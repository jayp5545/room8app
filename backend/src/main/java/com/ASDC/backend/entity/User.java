package com.ASDC.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany (mappedBy = "created_by", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "created-groceries")
    private List<GroceryList> groceryListCreatedBy;

    @OneToMany (mappedBy = "last_modified_by", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "modified-groceries")
    private List<GroceryList> groceryListLastModifiedBy;

    @OneToMany (mappedBy = "added_by", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "created-groceries_item")
    private List<GroceryItems> groceryListItemsCreatedBy;

    @OneToMany (mappedBy = "last_modified_by", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "modified-groceries_item")
    private List<GroceryItems> groceryListItemsLastModifiedBy;

    @OneToMany(mappedBy = "userPosted", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-posted-announcements")
    private List<Announcement> announcementsPosted;

    @OneToMany(mappedBy = "userModified", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-modified-announcements")
    private List<Announcement> announcementsModified;

    @OneToMany (mappedBy = "created_by")
    @JsonManagedReference(value = "created-expense")
    private List<Expense> expenseCreatedBy;

    @OneToMany (mappedBy = "last_modified_by")
    @JsonManagedReference(value = "modified-expense")
    private List<Expense> expenseLastModifiedBy;

    @OneToMany (mappedBy = "paid_by")
    @JsonManagedReference(value = "paid_by-expense")
    private List<Expense> expensePaidBy;

    @OneToMany (mappedBy = "user_id")
    @JsonManagedReference(value = "participant-user_id")
    private List<Participant> participantUserID;

    @OneToMany (mappedBy = "paid_by")
    @JsonManagedReference(value = "settleup-paid_by")
    private List<SettleUp> paid_by;

    @OneToMany (mappedBy = "paid_to")
    @JsonManagedReference(value = "settleup-paid_to")
    private List<SettleUp> paid_to;

    @OneToMany (mappedBy = "created_by")
    @JsonManagedReference(value = "created-settleup")
    private List<SettleUp> settleupCreatedBy;

    @OneToMany (mappedBy = "last_modified_by")
    @JsonManagedReference(value = "modified-settleup")
    private List<SettleUp> settleupLastModifiedBy;
}
