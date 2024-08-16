package com.ASDC.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn (name = "room_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference(value = "room-expense")
    private Room room;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime date_of_creation;

    @Column(nullable = false)
    private LocalDateTime last_modified_on;

    @ManyToOne
    @JsonBackReference(value = "created-expense")
    @JoinColumn (name = "created_by", referencedColumnName = "id", nullable = false)
    private User created_by;

    @ManyToOne
    @JsonBackReference(value = "modified-expense")
    @JoinColumn (name = "last_modified_by", referencedColumnName = "id", nullable = false)
    private User last_modified_by;

    @Column(nullable = false)
    private boolean status;

    @ManyToOne
    @JsonBackReference(value = "paid_by-expense")
    @JoinColumn (name = "paid_by", referencedColumnName = "id", nullable = false)
    private User paid_by;

    @OneToMany (mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "expense-participant")
    private List<Participant> participants;

    @OneToMany (mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "activity-expense")
    private List<Activity> activities;
}
