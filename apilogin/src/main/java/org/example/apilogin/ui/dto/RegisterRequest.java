package org.example.apilogin.ui.dto;

public record RegisterRequest(
    String username,
    String password,
    String email,
    String nombre
) {}
