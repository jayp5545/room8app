package com.ASDC.backend.dto.ResponseDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleUpAmountDTOResponse {

    private UserDTOResponse from;
    private UserDTOResponse to;
    private double amount;

    //If true=lent, false=owe
    private boolean status;

}
