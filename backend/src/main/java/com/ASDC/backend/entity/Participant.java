package com.ASDC.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participant")
public class Participant {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonBackReference(value = "participant-user_id")
    @JoinColumn (name = "user_id", referencedColumnName = "id", nullable = false)
    private User user_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "expense_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference(value = "expense-participant")
    private Expense expense;

    @Column(nullable = false)
    private double amount;

}
