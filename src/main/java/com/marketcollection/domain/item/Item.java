package com.marketcollection.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.common.exception.ErrorCode;
import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.common.BaseTimeEntity;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.order.exception.OutOfStockException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(indexes = {@Index(name = "idx_item_category", columnList = "categoryId"),
                @Index(name = "idx_category_item", columnList = "id, itemName, originalPrice, salePrice, repImageUrl, itemSaleStatus, categoryId"),
                @Index(name = "idx_item_modified_date", columnList = "modifiedDate")})
@Entity
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private int originalPrice;

    private int salePrice;

    private int discountPrice;

    private int stockQuantity;

    private String description;

    private Long categoryId;

    private String repImageUrl;
    private int salesCount;
    private int reviewCount;
    private int hit;

    @Enumerated(EnumType.STRING)
    private ItemSaleStatus itemSaleStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<ItemImage> itemImages = new ArrayList<>();

    @Builder
    public Item(String itemName, int originalPrice, int salePrice, int discountPrice, int stockQuantity, String description,
                Long categoryId, String repImageUrl, int salesCount, int reviewCount, int hit, ItemSaleStatus itemSaleStatus) {
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.discountPrice = discountPrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.categoryId = categoryId;
        this.repImageUrl = repImageUrl;
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.hit = hit;
        this.itemSaleStatus = itemSaleStatus;
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.originalPrice = itemFormDto.getOriginalPrice();
        this.salePrice = itemFormDto.getSalePrice();
        this.stockQuantity = itemFormDto.getStockQuantity();
        this.description = itemFormDto.getDescription();
        this.categoryId = itemFormDto.getCategoryId();
        this.itemSaleStatus = itemFormDto.getItemSaleStatus();
    }

    public void addHit() {
        this.hit++;
    }

    public void deductStock(int count) {
        int restStock = this.stockQuantity - count;
        if(restStock < 0) {
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK);
        }
        this.stockQuantity = restStock;
    }

    public void checkStock(int count) {
        int restStock = this.stockQuantity - count;
        if(restStock < 0) {
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK);
        }
    }

    public void addSalesCount(int count) {
        this.salesCount += count;
    }

    public void restoreStock(int count) {
        this.stockQuantity += count;
    }

    public void addRepImageUrl(String imageUrl) {
        this.repImageUrl = imageUrl;
    }

    public void addReviewCount() { this.reviewCount++; }

    public void setDiscountPrice(float discountRate) {
        this.discountPrice = (int) Math.round(this.salePrice * discountRate * 0.01);
    }
}
