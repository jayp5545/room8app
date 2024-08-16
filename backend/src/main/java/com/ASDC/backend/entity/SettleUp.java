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
@Table(name = "settleup")
public class SettleUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn (name = "room_id", referencedColumnName = "id")
    @JsonBackReference(value = "room-settleup")
    private Room room;

    @ManyToOne
    @JsonBackReference(value = "settleup-paid_by")
    @JoinColumn (name = "paid_by", referencedColumnName = "id")
    private User paid_by;

    @ManyToOne
    @JsonBackReference(value = "settleup-paid_to")
    @JoinColumn (name = "paid_to", referencedColumnName = "id")
    private User paid_to;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private LocalDateTime date_of_creation;

    @Column(nullable = false)
    private LocalDateTime last_modified_on;

    @ManyToOne
    @JsonBackReference(value = "created-settleup")
    @JoinColumn (name = "created_by", referencedColumnName = "id", nullable = false)
    private User created_by;

    @ManyToOne
    @JsonBackReference(value = "modified-settleup")
    @JoinColumn (name = "last_modified_by", referencedColumnName = "id", nullable = false)
    private User last_modified_by;

    @OneToMany (mappedBy = "settleUp", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "activity-settleup")
    private List<Activity> activities;
}

