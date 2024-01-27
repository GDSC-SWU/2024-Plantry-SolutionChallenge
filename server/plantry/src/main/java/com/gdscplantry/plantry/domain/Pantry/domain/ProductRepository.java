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
            "where p.pantryId = :pantryId and p.storage = :storage and p.date > current date " +
            "order by p.date desc ")
    LinkedList<ProductListItemResDto> findAllNotExpiredByPantryIdAndStorageOrderByDateByJPQL(Long pantryId, StorageEnum storage);

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


    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p) " +
            "from Product p " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.date > current date and p.name like %:query% " +
            "order by p.date desc ")
    LinkedList<ProductListItemResDto> findAllNotExpiredByPantryIdAndStorageAndQueryOrderByDateByJPQL(Long pantryId, StorageEnum storage, String query);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p) " +
            "from Product p " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.name like %:query% " +
            "and p.date < current date ")
    LinkedList<ProductListItemResDto> findAllExpiredByPantryIdAndStorageAndQueryByJPQL(Long pantryId, StorageEnum storage, String query);

    @Query(value = "select new com.gdscplantry.plantry.domain.Pantry.dto.product.ProductListItemResDto(p) " +
            "from Product p " +
            "where p.pantryId = :pantryId and p.storage = :storage and p.name like %:query% " +
            "and p.date = current date ")
    LinkedList<ProductListItemResDto> findAllDdayByPantryIdAndStorageAndQueryByJPQL(Long pantryId, StorageEnum storage, String query);

    void deleteAllByPantryId(Long pantryId);
}
