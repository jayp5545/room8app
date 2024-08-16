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
@Table (name = "grocery_items")
public class GroceryItems {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonBackReference(value = "grocery_items")
    @JoinColumn (name = "grocery_id", nullable = false)
    private GroceryList groceryList;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = true)
    private String note;

    @Column(nullable = false)
    private LocalDateTime added_on;

    @ManyToOne
    @JsonBackReference(value = "created-groceries_item")
    @JoinColumn (name = "added_by", nullable = false)
    private User added_by;

    @Column(nullable = false)
    private LocalDateTime last_modified_on;

    @ManyToOne
    @JsonBackReference(value = "modified-groceries_item")
    @JoinColumn (name = "last_modified_by", nullable = false)
    private User last_modified_by;

    @Column(nullable = false)
    private boolean purchased;

}
