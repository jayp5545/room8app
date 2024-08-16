package com.ASDC.backend.Models;


public interface SettleUpAmountDTOProjection {
    Integer getPaidBy();
    Integer getPaidTo();
    Double getAmount();
    Boolean getStatus();
}
