package com.marketcollection.domain.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
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

    @Builder
    public ItemImage(Item item, String originalFileName, String renamedFileName, String itemImageUrl) {
        this.item = item;
        this.originalFileName = originalFileName;
        this.renamedFileName = renamedFileName;
        this.itemImageUrl = itemImageUrl;
    }
}
