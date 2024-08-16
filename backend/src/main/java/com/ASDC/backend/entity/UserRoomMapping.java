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
@Table (name = "user_room")
public class UserRoomMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userid;

    @ManyToOne
    @JsonBackReference(value = "user_room")
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private Room roomid;

    @Column(nullable = false)
    private LocalDateTime join_date;

    private LocalDateTime remove_date;
}
