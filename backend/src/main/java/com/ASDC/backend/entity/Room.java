package com.ASDC.backend.entity;

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
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private int code;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date_of_creation;

    @Column(nullable = false)
    private int members;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "roomid", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user_room")
    private List<UserRoomMapping> userRoomMappings;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "room-groceries")
    private List<GroceryList> grocery;

    @OneToMany(mappedBy = "room")
    @JsonManagedReference(value = "room-expense")
    private List<Expense> expense;

    @OneToMany(mappedBy = "room")
    @JsonManagedReference(value = "room-settleup")
    private List<SettleUp> settleUp;

    @OneToMany(mappedBy = "room")
    @JsonManagedReference(value = "room-activity")
    private List<Activity> activities;
}
