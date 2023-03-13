package com.marketcollection.domain.review.dto;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewFormDto {
    private Long id;
    private Long itemId;
    private String itemName;
    private String repImageUrl;

    @NotBlank @Size(max = 1000, message = "리뷰 내용이 입력되지 않았습니다.")
    private String content;

    public ReviewFormDto(ItemFormDto itemFormDto) {
        this.itemId = itemFormDto.getId();
        this.itemName = itemFormDto.getItemName();
        this.repImageUrl = itemFormDto.getRepImageUrl();
    }
}
