package com.marketcollection.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReviewDto {
    private Long id;
    private Long itemId;
    private String itemName;
    private String memberName;
    private String memberGrade;
    private String repImageUrl;
    private int likes;

    @NotBlank @Size(max = 1000, message = "리뷰 내용이 입력되지 않았습니다.")
    private String content;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime createdDate;

    public ReviewDto(ItemFormDto itemFormDto) {
        this.itemId = itemFormDto.getId();
        this.itemName = itemFormDto.getItemName();
        this.repImageUrl = itemFormDto.getRepImageUrl();
    }
}
