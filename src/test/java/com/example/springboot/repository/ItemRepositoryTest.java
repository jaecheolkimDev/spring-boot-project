package com.example.springboot.repository;

import com.example.springboot.code.ItemSellStatus;
import com.example.springboot.entity.ItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "springdoc.api-docs.enabled=false")
//@TestPropertySource(locations = "classpath:application-test.yml") // 어노테이션이 역할을 제대로 못함
@ActiveProfiles("test") // application-test.yml을 선택함
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setItemNm("테스트 상품");
        itemEntity.setPrice(10000);
        itemEntity.setItemDetail("테스트 상품 상세 설명");
        itemEntity.setItemSellStatus(ItemSellStatus.SELL);
        itemEntity.setStockNumber(100);
        itemEntity.setRegTime(LocalDateTime.now());
        itemEntity.setUpdateTime(LocalDateTime.now());
        ItemEntity savedItemEntity = itemRepository.save(itemEntity);
        System.out.println(savedItemEntity.toString());
    }

    public void createItemList() {
        for(int i=1; i<=10; i++) {
            ItemEntity item = new ItemEntity();
            item.setItemNm("테스트 상품" +  i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            ItemEntity savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<ItemEntity> itemEntityList = itemRepository.findByItemNm("테스트 상품1");
        for(ItemEntity item : itemEntityList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<ItemEntity> itemEntityList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(ItemEntity item : itemEntityList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<ItemEntity> itemEntityList = itemRepository.findByPriceLessThan(10005);
        for(ItemEntity item : itemEntityList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<ItemEntity> itemEntityList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(ItemEntity item : itemEntityList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTestByQueryAnnotation() {
        this.createItemList();
        List<ItemEntity> itemEntityList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(ItemEntity item : itemEntityList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailTestByNativeQuery() {
        this.createItemList();
        List<ItemEntity> itemEntityList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(ItemEntity item : itemEntityList) {
            System.out.println(item.toString());
        }
    }
}