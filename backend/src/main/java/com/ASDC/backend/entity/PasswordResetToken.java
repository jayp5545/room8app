package com.ASDC.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.spi.Tokens;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

}
