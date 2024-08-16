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
@Table(name = "announcements")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "posted_date", nullable = false)
    private LocalDateTime postedDateTime;

    @Column(name = "last_update_date_time")
    private LocalDateTime lastUpdatedDateTime;

    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "room-announcements")
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "user-posted-announcements")
    @JoinColumn(name = "user_posted_id", referencedColumnName = "id", nullable = false)
    private User userPosted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "user-modified-announcements")
    @JoinColumn(name = "user_modified_id", referencedColumnName = "id")
    private User userModified;
}


