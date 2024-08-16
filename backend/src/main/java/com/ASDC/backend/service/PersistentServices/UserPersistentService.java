package com.ASDC.backend.service.PersistentServices;

import com.ASDC.backend.entity.User;
import com.ASDC.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersistentService  {

    private final UserRepository userRepository;

    public UserPersistentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty() || !optionalUser.isPresent()){
            return null;
        }
        return optionalUser.get();
    }

    public User saveUser(User user){
        User savedUser = userRepository.save(user);
        return savedUser;

    }
}
