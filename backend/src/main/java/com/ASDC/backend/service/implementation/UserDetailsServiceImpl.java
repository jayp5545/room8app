package com.ASDC.backend.service.implementation;

import com.ASDC.backend.config.CustomUserDetails;
import com.ASDC.backend.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ASDC.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    /**
     * Loads the user by username.
     *
     * @param email the username of the user
     * @return UserDetails
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", email);
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("Wrong email" + email);
        }
        return new CustomUserDetails(user.get());
    }
}

