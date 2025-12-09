package org.example.apilogin.ui.dto;

public record RegisterDTO(
    String username,
    String password,
    String email,
    String nombre
) {}
