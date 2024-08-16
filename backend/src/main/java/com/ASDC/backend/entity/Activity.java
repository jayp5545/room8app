
package com.ASDC.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity")
public class Activity {

    public enum ActivityType {
        Expense_ADD,
        Expense_EDIT,
        Expense_DELETE,
        SETTLE_UP_Add,
        SETTLE_UP_EDIT,
        SETTLE_UP_DELETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    @ManyToOne
    @JoinColumn (name = "expense_id", referencedColumnName = "id")
    @JsonBackReference(value = "activity-expense")
    private Expense expense;

    @ManyToOne
    @JoinColumn (name = "settleup_id", referencedColumnName = "id")
    @JsonBackReference(value = "activity-settleup")
    private SettleUp settleUp;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String datails;

    @ManyToOne
    @JoinColumn (name = "room_id", referencedColumnName = "id")
    @JsonBackReference(value = "room-activity")
    private Room room;
}
