package com.marketcollection.domain.item;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class ItemImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    private String originalFileName;
    private String renamedFileName;
    private String itemImageUrl;
}
