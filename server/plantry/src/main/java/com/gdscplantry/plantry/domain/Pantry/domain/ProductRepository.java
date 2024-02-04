package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p, case when count(n) > 0 then true else false end) " +
            "from Product p left join fetch Notification n on n.user = :user and p.id = n.entityId and n.typeKey < 20 and n.isOff = false " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.date > current date " +
            "group by p.id " +
            "order by p.date asc ")
    LinkedList<ProductListItemResDto> findAllNotExpiredByPantryIdAndStorageOrderByDateByJPQL(User user, Long pantryId, StorageEnum storage);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p, case when count(n) > 0 then true else false end) " +
            "from Product p left join fetch Notification n on n.user = :user and p.id = n.entityId and n.typeKey < 20 and n.isOff = false " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.date < current date " +
            "group by p.id " +
            "order by p.date asc ")
    LinkedList<ProductListItemResDto> findAllExpiredByPantryIdAndStorageByJPQL(User user, Long pantryId, StorageEnum storage);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p, case when count(n) > 0 then true else false end) " +
            "from Product p left join fetch Notification n on n.user = :user and p.id = n.entityId and n.typeKey < 20 and n.isOff = false " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.date = current date " +
            "group by p.id " +
            "order by p.date asc ")
    LinkedList<ProductListItemResDto> findAllDdayByPantryIdAndStorageByJPQL(User user, Long pantryId, StorageEnum storage);


    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p, case when count(n) > 0 then true else false end) " +
            "from Product p left join fetch Notification n on n.user = :user and p.id = n.entityId and n.typeKey < 20 and n.isOff = false " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.date > current date and p.name like %:query% " +
            "group by p.id " +
            "order by p.date asc ")
    LinkedList<ProductListItemResDto> findAllNotExpiredByPantryIdAndStorageAndQueryOrderByDateByJPQL(User user, Long pantryId, StorageEnum storage, String query);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p, case when count(n) > 0 then true else false end) " +
            "from Product p left join fetch Notification n on n.user = :user and p.id = n.entityId and n.typeKey < 20 and n.isOff = false " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.date < current date and p.name like %:query% " +
            "group by p.id " +
            "order by p.date asc ")
    LinkedList<ProductListItemResDto> findAllExpiredByPantryIdAndStorageAndQueryByJPQL(User user, Long pantryId, StorageEnum storage, String query);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p, case when count(n) > 0 then true else false end) " +
            "from Product p left join fetch Notification n on n.user = :user and p.id = n.entityId and n.typeKey < 20 and n.isOff = false " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.date = current date and p.name like %:query% " +
            "group by p.id " +
            "order by p.date asc ")
    LinkedList<ProductListItemResDto> findAllDdayByPantryIdAndStorageAndQueryByJPQL(User user, Long pantryId, StorageEnum storage, String query);

    void deleteAllByPantryId(Long pantryId);
}
