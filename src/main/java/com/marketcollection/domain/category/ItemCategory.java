package com.marketcollection.domain.category;

import lombok.*;

import javax.persistence.*;
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class ItemCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    private Long parentId;

    @Builder
    public ItemCategory(String categoryName, Long parentId) {
        this.categoryName = categoryName;
        this.parentId = parentId;
    }
}
