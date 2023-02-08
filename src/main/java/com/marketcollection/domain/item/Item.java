package com.marketcollection.domain.item;

import com.marketcollection.domain.common.BaseEntity;
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
    private Long categoryId;

    private int salesCount;
    private int hit;
    private ItemSaleStatus itemSaleStatus;

    @Builder
    public Item(String itemName, int originalPrice, int salePrice, int stockQuantity, String description, String thumbnailImageFile, Long categoryId, int salesCount, int hit, ItemSaleStatus itemSaleStatus) {
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.thumbnailImageFile = thumbnailImageFile;
        this.categoryId = categoryId;
        this.salesCount = salesCount;
        this.hit = hit;
        this.itemSaleStatus = itemSaleStatus;
    }

    public void setThumbnailImageFile(String thumbnailImageFile) {
        this.thumbnailImageFile = thumbnailImageFile;
    }
}
