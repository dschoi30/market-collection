package com.marketcollection.domain.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PageCursor<T> {
    private List<T> values;
    private Boolean hasNext;

    public PageCursor(List<T> values, Boolean hasNext) {
        this.values = values;
        this.hasNext = hasNext;
    }
}
