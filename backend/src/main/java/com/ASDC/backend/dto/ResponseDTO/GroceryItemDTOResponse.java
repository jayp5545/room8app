package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItemDTOResponse {

    private int id;
    private String name;
    private int quantity;
    private String note;
    private LocalDateTime added_on;
    private LocalDateTime last_modified_on;
    private boolean purchased;
    private UserDTOResponse added_by;
    private UserDTOResponse last_modified_by;
    private String groceryListName;

}
