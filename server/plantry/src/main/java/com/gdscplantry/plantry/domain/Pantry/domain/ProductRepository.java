package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p) " +
            "from Product p " +
            "where p.pantryId = :pantryId and p.storage = :storageEnum and p.date > current date " +
            "order by p.date desc ")
    LinkedList<ProductListItemResDto> findAllNotExpiredByPantryIdAndStorageOrderByDateByJPQL(Long pantryId, StorageEnum storageEnum);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p) " +
            "from Product p " +
            "where p.pantryId = :pantryId and p.storage = :storage " +
            "and p.date < current date ")
    LinkedList<ProductListItemResDto> findAllExpiredByPantryIdAndStorageByJPQL(Long pantryId, StorageEnum storage);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p) " +
            "from Product p " +
            "where p.pantryId = :pantryId and p.storage = :storage " +
            "and p.date = current date ")
    LinkedList<ProductListItemResDto> findAllDdayByPantryIdAndStorageByJPQL(Long pantryId, StorageEnum storage);
}
