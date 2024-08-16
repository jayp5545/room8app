//package com.ASDC.backend.service.PersistentServices;
//
//import com.ASDC.backend.entity.Task;
//import com.ASDC.backend.repository.TaskRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class TaskPersistentService {
//    private final TaskRepository taskRepository;
//
//    public TaskPersistentService(TaskRepository taskRepository) {
//        this.taskRepository = taskRepository;
//    }
//
//    public List<> findAllAnnouncementsByRoom(int id){
//        List<Announcement> announcements = announcementRepository.findAllAnnouncementsByRoom(id);
//        return announcements;
//    }
//
//    public Announcement saveAnnouncement(Announcement announcement){
//        Announcement savedAnnouncement = announcementRepository.save(announcement);
//        return savedAnnouncement;
//    }
//
//    public Optional<Announcement> findById(Long id){
//        return announcementRepository.findById(id);
//    }
//
//    public Optional<Announcement> findByTitle(String title){
//        return announcementRepository.findByTitle(title);
//    }
//    public boolean existsById(Long id){
//        return announcementRepository.existsById(id);
//    }
//    public void deleteAnnouncement(Announcement announcement){
//        announcementRepository.delete(announcement);
//    }
//
//}
