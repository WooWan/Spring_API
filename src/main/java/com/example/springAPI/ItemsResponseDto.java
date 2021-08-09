package com.example.springAPI;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemsResponseDto {

    private Long id;
    private String itemName;
    private int itemQuantity;

    public ItemsResponseDto(Item entity) {
        this.id = entity.getId();
        this.itemName = entity.getItemName();
        this.itemQuantity = entity.getItemQuantity();
    }

}
