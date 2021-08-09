package com.example.springAPI;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemsSaveDto {

    private String title;
    private int itemQuantity;

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .itemQuantity(itemQuantity)
                .build();
    }


}
