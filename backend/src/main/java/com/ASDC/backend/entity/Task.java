package com.ASDC.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn (name = "room_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference(value = "room-tasks")
    private Room room;

    @Column(name = "name")
    private String name;

    @Column(nullable = false)
    private LocalDateTime date_of_creation;

    @Column(nullable = false)
    private LocalDateTime last_modified_on;

    @Column(name = "task_date")
    private LocalDate taskDate;

    @ManyToOne
    @JsonBackReference(value = "created-tasks")
    @JoinColumn (name = "created_by", referencedColumnName = "id", nullable = false)
    private User created_by;

    @ManyToOne
    @JsonBackReference(value = "modified-tasks")
    @JoinColumn (name = "last_modified_by", referencedColumnName = "id", nullable = false)
    private User last_modified_by;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonBackReference(value = "assigned-person")
    @JoinColumn (name = "assigned_to", referencedColumnName = "id", nullable = false)
    private User assigned_to;

    @Column(nullable = false)
    private boolean finished;
}
