package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.entity.Participant;
import com.ASDC.backend.exception.CustomException;
import com.ASDC.backend.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ParticipantPersistentService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantPersistentService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> saveParticipants(List<Participant> participants) {
        return participantRepository.saveAll(participants);
    }

    public void deleteParticipants(List<Participant> participants){
        for (Participant currParticipant : participants){
            if (!participantRepository.existsById(currParticipant.getId())) {
                throw new CustomException("400", "Participant with ID " + currParticipant.getId() + " does not exist.");
            }
            participantRepository.deleteById(currParticipant.getId());
        }
    }
}
