package com.ASDC.backend.config;

import com.ASDC.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsTest {

    @Test
    public void customUserDetailsTest() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test1");
        user.setEmail("test@test.com");
        user.setPassword("testPass");

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        assertEquals("test@test.com", customUserDetails.getUsername());
        assertEquals("testPass", customUserDetails.getPassword());
        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isEnabled());

        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        assertTrue(authorities.isEmpty());
    }
}
