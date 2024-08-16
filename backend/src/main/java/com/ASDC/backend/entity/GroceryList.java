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
@Table (name = "grocery")
public class GroceryList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn (name = "room_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference(value = "room-groceries")
    private Room room;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int items;

    @Column(nullable = false)
    private int items_purchased;

    @Column(nullable = false)
    private LocalDateTime date_of_creation;

    @Column(nullable = false)
    private LocalDateTime last_modified_on;

    @ManyToOne
    @JsonBackReference(value = "created-groceries")
    @JoinColumn (name = "created_by", referencedColumnName = "id", nullable = false)
    private User created_by;

    @ManyToOne
    @JsonBackReference(value = "modified-groceries")
    @JoinColumn (name = "last_modified_by", referencedColumnName = "id", nullable = false)
    private User last_modified_by;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "groceryList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "grocery_items")
    private List<GroceryItems> grocery_items;

}
