package org.example.apilogin.ui.dto;

import java.time.LocalDateTime;

public record SharedSecretResponse(
        Long id,
        Long sharedWithId,
        LocalDateTime createdAt
) {}
