package com.ASDC.backend.dto.RequestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItemDTORequest {

    private int id;

    @NotBlank(message = "Item name can't be null or empty!")
    private String name;

    @Min(value = 1, message = "Quantity must be greater than zero!")
    private int quantity;

    @NotBlank(message = "Grocery list name can't be null or empty!")
    private String groceryListName;

    private String note;
}
