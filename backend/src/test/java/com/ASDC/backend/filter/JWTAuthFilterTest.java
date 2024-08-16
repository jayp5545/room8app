package com.ASDC.backend.filter;

import com.ASDC.backend.service.implementation.UserDetailsServiceImpl;
import com.ASDC.backend.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


public class JWTAuthFilterTest {

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private JWTAuthFilter jwtAuthFilter;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void doFilterInternalTestValid() throws ServletException, IOException {
        String token = "validToken";
        String email = "demo@demo.com";
        UserDetails userDetails = mock(UserDetails.class);

        request.addHeader("Authorization", "Bearer " + token);
        when(jwtUtil.getEmail(token)).thenReturn(email);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        assertEquals(authToken, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }


    @Test
    public void doFilterTestInvalidToken() throws ServletException, IOException {
        String token = "invalidToken";

        request.addHeader("Authorization", "Bearer " + token);
        when(jwtUtil.getEmail(token)).thenReturn("invalidemail");
        when(jwtUtil.validateToken(token)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterTestNoToken() throws ServletException, IOException {
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
