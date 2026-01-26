package com.example.springboot.repository;

import com.example.springboot.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByItemNm(String itemNm);

    List<ItemEntity> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<ItemEntity> findByPriceLessThan(Integer price);

    List<ItemEntity> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from ItemEntity i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<ItemEntity> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<ItemEntity> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
