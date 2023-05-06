package com.marketcollection.domain.discount.service;

import com.marketcollection.domain.discount.ItemDiscount;
import com.marketcollection.domain.discount.DiscountType;
import com.marketcollection.domain.discount.dto.DiscountRequestDto;
import com.marketcollection.domain.discount.repository.DiscountRepository;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DiscountServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired DiscountService discountService;
    @Autowired DiscountRepository discountRepository;

    public Item saveItem() {
        Item item = new Item("test1", 10000, 9000, 10000, "content",
                1L, "", 0, 0, 0, ItemSaleStatus.ON_SALE);
        return itemRepository.save(item);
    }

    @DisplayName("일일 특가 상품 등록 테스트")
    @Test
    public void saveDiscountItem() {
        //given
        Item item = saveItem();
        List<Long> itemIds = new ArrayList<>();
        itemIds.add(item.getId());
        DiscountRequestDto dto = new DiscountRequestDto(itemIds, DiscountType.DAILY_SALE, 10, LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        //when
        Long discountItemId = discountService.saveDiscount(dto);

        //then
        ItemDiscount itemDiscount = discountRepository.findById(discountItemId).orElseThrow(EntityNotFoundException::new);
        Assertions.assertThat(itemDiscount.getDiscountPrice()).isEqualTo(900);
    }
}