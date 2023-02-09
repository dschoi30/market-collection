package com.marketcollection.common.main;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Component
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
            initService.dbInit1();
    }

    @RequiredArgsConstructor
    @Transactional
    @Component
    public static class InitService {
        private final EntityManager em;

        public void dbInit1() {

            for(int i = 0; i < 100; i++) {
                Item item = Item.builder()
                        .itemName("향기가득 샤인머스캣_" + i)
                        .originalPrice(10000)
                        .salePrice(10000 - i * 100)
                        .stockQuantity(1000)
                        .description("너무 맛있어요")
                        .thumbnailImageFile("/images/items/grape1.jpg")
                        .categoryId(1L)
                        .itemSaleStatus(ItemSaleStatus.ON_SALE)
                        .build();
                em.persist(item);
                for(int j = 0; j < 3; j++) {
                    ItemImage itemImage = new ItemImage(item, "grape2", "grape2", "/images/items/grape2.jpg");
                    em.persist(itemImage);
                }
            }
        }
    }
}
