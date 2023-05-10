package com.marketcollection.domain.discount.service;

import com.marketcollection.domain.discount.ItemDiscount;
import com.marketcollection.domain.discount.dto.DiscountRequestDto;
import com.marketcollection.domain.discount.dto.DiscountResponseDto;
import com.marketcollection.domain.discount.repository.DiscountRepository;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class DiscountService {

    private final ItemRepository itemRepository;
    private final DiscountRepository discountRepository;

    public Long saveDiscount(DiscountRequestDto dto) {
        Long discountItemId = 0L;
        for(Long itemId : dto.getItemIds()) {
            Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
            item.setDiscountPrice(dto.getDiscountRate());

            ItemDiscount itemDiscount = ItemDiscount.createFlashDiscountItem(item, dto);
            ItemDiscount savedItemDiscount = discountRepository.save(itemDiscount);
            discountItemId = savedItemDiscount.getId();
        }
        return discountItemId;
    }

    @Transactional(readOnly = true)
    public Page<DiscountResponseDto> getItemDiscountList(Pageable pageable) {
        return discountRepository.getItemDiscountList(pageable);
    }

    public void finishItemDiscount(List<Long> itemDiscountIds) {
        for(Long id : itemDiscountIds) {
            ItemDiscount itemDiscount = discountRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            itemDiscount.offSale();
            Item item = itemRepository.findById(itemDiscount.getItem().getId()).orElseThrow(EntityNotFoundException::new);
            item.setDiscountPrice(0);
        };
    }

    // 한 시간 주기로 할인 종료 여부 확인 후 처리
    @Scheduled(cron = "0 0 0/1 * * *")
    public void finishSale() {
        List<ItemDiscount> itemDiscounts = discountRepository.findOnGoingItemDiscount();
        for(ItemDiscount itemDiscount : itemDiscounts) {
            if(itemDiscount.getFinishDate().isBefore(LocalDateTime.now())) {
                itemDiscount.offSale();
                Item item = itemRepository.findById(itemDiscount.getItem().getId()).orElseThrow(EntityNotFoundException::new);
                item.setDiscountPrice(0);
            }
        }
    }
}