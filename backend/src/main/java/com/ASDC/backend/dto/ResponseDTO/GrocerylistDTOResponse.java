package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrocerylistDTOResponse {

    private int id;
    private String name;
    private int items;
    private int items_purchased;
    private LocalDateTime date_of_creation;
    private LocalDateTime last_modified_on;
    private boolean active;
    private List<GroceryItemDTOResponse> grocery_items;
    private RoomDTOResponse room;
    private UserDTOResponse created_by;
    private UserDTOResponse last_modified_by;
}
