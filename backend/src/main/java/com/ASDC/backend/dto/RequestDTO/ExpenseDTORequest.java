package com.ASDC.backend.dto.RequestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTORequest {

    private int id;

    @NotBlank(message = "Expense description can't be null or empty!")
    private String description;

    @Min(value = 1, message = "Expense amount must be greater than zero!")
    private double amount;

    @NotBlank(message = "Paid by email can't be null or empty!")
    private String paidBy;

    @NotNull(message = "Participant list can't be null!")
    @Size(min = 1, message = "Participant list size must be greater than 0")
    private List<String> participantEmails;
}