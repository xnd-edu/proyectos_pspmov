package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Secret;
import org.example.apilogin.domain.service.SecretsService;
import org.example.apilogin.ui.dto.SecretDTO;
import org.example.apilogin.ui.dto.SecretResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.API_SECRETS)
public class SecretsController {
    private final SecretsService secretsService;

    public SecretsController(SecretsService secretsService) {
        this.secretsService = secretsService;
    }

    @GetMapping
    public ResponseEntity<List<Secret>> getUserSecrets(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(secretsService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<SecretResponse> createSecret(Authentication authentication, @RequestBody SecretDTO secret) {
        Long userId = Long.parseLong(authentication.getName());
        Secret newSecret = new Secret(null, userId, secret.encryptedData(), secret.iv(), secret.salt(), null);
        newSecret = secretsService.save(newSecret);
        return ResponseEntity.ok(new SecretResponse(newSecret.id(), newSecret.createdAt()));
    }
}
