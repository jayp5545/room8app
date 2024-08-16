package com.ASDC.backend.repository;

import com.ASDC.backend.entity.Announcement;
import com.ASDC.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Optional<Announcement> findByTitle(String title);

    @Query(value = "SELECT * FROM announcements where room_id = :roomID",nativeQuery = true)
    List<Announcement> findAllAnnouncementsByRoom(@Param("roomID") int roomID);

    Optional<Announcement> getAnnouncementById(Long id);

}

