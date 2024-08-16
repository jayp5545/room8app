package com.ASDC.backend.dto.RequestDTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleUpDTORequest {

    private int id;

    @NotBlank(message = "Settle-Up receiver email can't be Null or Empty!")
    private String to;

    @Min(value = 1, message = "Expense amount must be greater than zero!")
    private double amount;

}
