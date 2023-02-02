package com.marketcollection.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.common.entity.Category;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

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
}
