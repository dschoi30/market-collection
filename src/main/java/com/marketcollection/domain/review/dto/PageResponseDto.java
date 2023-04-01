package com.marketcollection.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class PageResponseDto<E> {

    private int page;
    private int size;
    private int total;

    private int start;
    private int end;

    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    @Builder
    public PageResponseDto(PageRequestDto pageRequestDto, List<E> dtoList, int total) {
        if(total <= 0) {
            return;
        }

        this.page = pageRequestDto.getPage();
        this.size = pageRequestDto.getSize();

        this.dtoList = dtoList;
        this.total = total;

        this.end =   (int)(Math.ceil(this.page / 10.0 )) *  10;

        this.start = this.end - 9;

        int last =  (int)(Math.ceil((total/(double)size)));

        this.end =  end > last ? last: end;

        this.prev = this.start > 1;

        this.next =  total > this.end * this.size;
    }
}
