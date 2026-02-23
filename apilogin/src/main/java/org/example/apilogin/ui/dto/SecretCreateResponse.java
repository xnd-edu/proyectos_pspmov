package org.example.apilogin.ui.dto;

import java.time.LocalDateTime;

public record SecretCreateResponse(
        Long id,
        LocalDateTime createdAt
) {}
