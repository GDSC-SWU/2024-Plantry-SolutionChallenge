package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.Notification.service.RelatedNotificationService;
import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import com.gdscplantry.plantry.domain.Pantry.domain.ProductRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.product.*;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.Pantry.vo.FoodDataVo;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.tracker.ConsumedProduct;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.tracker.ConsumedProductRepository;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.ProductDeleteTypeEnum;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.gdscplantry.plantry.global.util.FoodDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final PantryService pantryService;
    private final UserPantryRepository userPantryRepository;
    private final ConsumedProductRepository consumedProductRepository;
    private final FoodDataUtil foodDataUtil;
    private final RelatedNotificationService relatedNotificationService;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public Product validateProductId(User user, Long productId) {
        // Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(PantryErrorCode.PRODUCT_NOT_FOUND));

        // Check access rights
        if (!userPantryRepository.existsByPantryIdAndUser(product.getPantryId(), user))
            throw new AppException(PantryErrorCode.PRODUCT_ACCESS_DENIED);

        return product;
    }

    public Map<Long, List<ProductListItemResDto>> groupProductList(LinkedList<ProductListItemResDto> expiredList, LinkedList<ProductListItemResDto> ddayList, LinkedList<ProductListItemResDto> notExpiredList) {
        Map<Long, List<ProductListItemResDto>> notExpiredMap = notExpiredList.stream()
                .collect(Collectors.groupingBy(ProductListItemResDto::getDays));
        Map<Long, List<ProductListItemResDto>> result = new HashMap<>();
        result.put(-1L, expiredList);
        result.put(0L, ddayList);
        result.putAll(notExpiredMap);

        return result;
    }

    @Transactional
    public ProductItemResDto addSingleProduct(User user, NewProductReqDto dto) {
        // Find pantry & Check access rights
        pantryService.validatePantryId(user, dto.getPantry());

        // Entity
        Product product = dto.toEntity();

        // Find from Food Database
        FoodDataVo foodDataVo = foodDataUtil.findFromFoodDatabase(product.getName(), LocalDate.now());
        product.updateFoodData(foodDataVo);

        // Save data
        productRepository.save(product);

        // Save default Notifications
        notificationRepository.saveAll(relatedNotificationService.addDefaultExpNotification(user, product));

        return new ProductItemResDto(product, true);
    }


    @Transactional
    public NewProductListResDto addProducts(User user, NewProductListReqDto dto) {
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Notification> notifications = new ArrayList<>();

        for (NewProductReqDto req : dto.getList()) {
            Product product = req.toEntity();

            // Find pantry & Check access rights
            pantryService.validatePantryId(user, req.getPantry());

            // Find from Food Database
            FoodDataVo foodDataVo = foodDataUtil.findFromFoodDatabase(req.getName(), LocalDate.now());
            product.updateFoodData(foodDataVo);

            // Add data
            products.add(product);

            // Add default Notifications
            notifications.addAll(relatedNotificationService.addDefaultExpNotification(user, product));
        }

        // Save product data
        productRepository.saveAll(products);

        // Save notification data
        notificationRepository.saveAll(notifications);

        // Return dto
        ArrayList<ProductItemResDto> result = new ArrayList<>();
        for (Product product : products)
            result.add(new ProductItemResDto(product, true));

        return new NewProductListResDto(result);
    }

    @Transactional
    public ProductItemResDto updateProduct(User user, Long productId, UpdateProductReqDto updateProductReqDto) {
        // Find product & check access rights
        Product product = validateProductId(user, productId);

        // Update product data
        product.updateProduct(updateProductReqDto.toEntity());

        // Check and update notifications
        boolean isNotified = relatedNotificationService.updateProduct(user, product);

        return new ProductItemResDto(product, isNotified);
    }

    @Transactional
    public ProductItemResDto updateProductCount(User user, Long productId, Double count) {
        // Find product & check access rights
        Product product = validateProductId(user, productId);

        // Count validation (1 / 0.5)
        if (count % 0.5 != 0 || count <= 0)
            throw new AppException(PantryErrorCode.INVALID_COUNT);

        // Update product data
        product.updateCount(BigDecimal.valueOf(count));

        // Check Notifications
        boolean isNotified = notificationRepository.existsAllByUserAndEntityIdAndIsOffAndTypeKeyLessThan(user, productId, false, 20);

        return new ProductItemResDto(product, isNotified);
    }

    @Transactional
    public DeleteProductResDto deleteProduct(User user, Long productId, Integer type, Double reqCount) {
        // Find product & check access rights
        Product product = validateProductId(user, productId);

        // Count validation
        BigDecimal count = BigDecimal.valueOf(reqCount);
        if (reqCount % 0.5 != 0 || reqCount <= 0)
            throw new AppException(PantryErrorCode.INVALID_COUNT);
        else if (count.compareTo(product.getCount()) > 0)
            throw new AppException(PantryErrorCode.INVALID_DELETE_COUNT);

        // Find type
        ProductDeleteTypeEnum typeEnum = ProductDeleteTypeEnum.findByKey(type);

        // Add consumption data
        consumedProductRepository.save(ConsumedProduct.builder()
                .user(user)
                .count(count)
                .type(typeEnum)
                .product(product.getName())
                .foodDataId(product.getFoodDataId())
                .addedAt(product.getCreatedAt())
                .build()
        );

        // Update product data (delete if result count is 0)
        BigDecimal result = product.getCount().subtract(count);
        if (result.equals(BigDecimal.ZERO)) {
            // Delete product
            productRepository.delete(product);

            // Delete product notifications
            notificationRepository.deleteAllByEntityIdAndTypeKeyLessThan(productId, 10);

        } else
            product.updateCount(result);

        return new DeleteProductResDto(product.getId(), typeEnum.getTitle(), result);
    }

    @Transactional(readOnly = true)
    public ProductListResDto readProductList(User user, Long pantryId, String filter) {
        // Find pantry & Check access rights
        pantryService.validatePantryId(user, pantryId);

        // Validate filter string
        StorageEnum storageEnum = StorageEnum.findByKey(filter);

        // Find product list
        LinkedList<ProductListItemResDto> expiredList = productRepository.findAllExpiredByPantryIdAndStorageByJPQL(user, pantryId, storageEnum);
        LinkedList<ProductListItemResDto> ddayList = productRepository.findAllDdayByPantryIdAndStorageByJPQL(user, pantryId, storageEnum);
        LinkedList<ProductListItemResDto> notExpiredList = productRepository.findAllNotExpiredByPantryIdAndStorageOrderByDateByJPQL(user, pantryId, storageEnum);

        // Group lists by day
        Map<Long, List<ProductListItemResDto>> result = groupProductList(expiredList, ddayList, notExpiredList);

        return new ProductListResDto(filter, result);
    }

    @Transactional
    public ProductListResDto productSearchList(User user, Long pantryId, String filter, String query) {
        // Find pantry & Check access rights
        pantryService.validatePantryId(user, pantryId);

        // Validate filter string
        StorageEnum storageEnum = StorageEnum.findByKey(filter);

        // Find product list
        LinkedList<ProductListItemResDto> expiredList = productRepository.findAllExpiredByPantryIdAndStorageAndQueryByJPQL(user, pantryId, storageEnum, query);
        LinkedList<ProductListItemResDto> ddayList = productRepository.findAllDdayByPantryIdAndStorageAndQueryByJPQL(user, pantryId, storageEnum, query);
        LinkedList<ProductListItemResDto> notExpiredList = productRepository.findAllNotExpiredByPantryIdAndStorageAndQueryOrderByDateByJPQL(user, pantryId, storageEnum, query);

        // Group lists by day
        Map<Long, List<ProductListItemResDto>> result = groupProductList(expiredList, ddayList, notExpiredList);

        return new ProductListResDto(filter, result);
    }
}
