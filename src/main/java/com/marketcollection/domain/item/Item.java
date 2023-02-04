package com.marketcollection.domain.item;

import com.marketcollection.api.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@NoArgsConstructor
@Getter
@Entity
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int stockQuantity;
    private String description;
    private String thumbnailImageFile;
    private Category category;

    private int salesCount;
    private int hit;
    private ItemSaleStatus itemSaleStatus;

    @Builder
    public Item(String itemName, int originalPrice, int salePrice, int stockQuantity, String description, String thumbnailImageFile, Category category, int salesCount, int hit, ItemSaleStatus itemSaleStatus) {
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.thumbnailImageFile = thumbnailImageFile;
        this.category = category;
        this.salesCount = salesCount;
        this.hit = hit;
        this.itemSaleStatus = itemSaleStatus;
    }
}
