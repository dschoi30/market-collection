package com.marketcollection.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.common.entity.Category;
import lombok.Getter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int stockQuantity;
    private String description;
    private String thumbnailImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
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
