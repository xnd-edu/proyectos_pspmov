package org.example.apilogin.ui.dto;

import java.time.LocalDateTime;

public record SecretResponse(
        Long id,
        LocalDateTime createdAt
) {}
