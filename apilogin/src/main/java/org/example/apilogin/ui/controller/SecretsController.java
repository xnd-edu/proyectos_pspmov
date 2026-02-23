package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Secret;
import org.example.apilogin.domain.service.SecretsService;
import org.example.apilogin.ui.dto.SecretDTO;
import org.example.apilogin.ui.dto.SecretCreateResponse;
import org.example.apilogin.ui.dto.SecretResponse;
import org.example.apilogin.ui.security.crypto.SymmetricEncryptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(Constantes.API_SECRETS)
public class SecretsController {
    private final SecretsService secretsService;
    private final SymmetricEncryptionService symmetricEncryptionService;

    public SecretsController(SecretsService secretsService, SymmetricEncryptionService symmetricEncryptionService) {
        this.secretsService = secretsService;
        this.symmetricEncryptionService = symmetricEncryptionService;
    }

    @GetMapping
    public ResponseEntity<List<SecretResponse>> getUserSecrets(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<Secret> secrets = secretsService.findByUserId(userId);
        List<SecretResponse> response = secrets.stream()
                .map(secret -> new SecretResponse(
                        secret.id(),
                        secret.userId(),
                        symmetricEncryptionService.bytesToBase64(secret.encryptedData()),
                        symmetricEncryptionService.bytesToBase64(secret.iv()),
                        symmetricEncryptionService.bytesToBase64(secret.salt()),
                        secret.createdAt()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping(Constantes.API_BY_ID)
    public ResponseEntity<SecretResponse> getSecretById(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        Secret secret = secretsService.findById(id);

        if (!secret.userId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SecretResponse response = new SecretResponse(
                secret.id(),
                secret.userId(),
                symmetricEncryptionService.bytesToBase64(secret.encryptedData()),
                symmetricEncryptionService.bytesToBase64(secret.iv()),
                symmetricEncryptionService.bytesToBase64(secret.salt()),
                secret.createdAt()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SecretCreateResponse> createSecret(Authentication authentication, @RequestBody SecretDTO secret) {
        Long userId = Long.parseLong(authentication.getName());

        byte[] encryptedData = symmetricEncryptionService.base64ToBytes(secret.encryptedDataBase64());
        byte[] iv = symmetricEncryptionService.base64ToBytes(secret.ivBase64());
        byte[] salt = symmetricEncryptionService.base64ToBytes(secret.saltBase64());

        Secret newSecret = new Secret(null, userId, encryptedData, iv, salt, null);
        newSecret = secretsService.save(newSecret);

        if (Objects.isNull(newSecret.id())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(new SecretCreateResponse(newSecret.id(), newSecret.createdAt()));
    }

    @DeleteMapping(Constantes.API_BY_ID)
    public ResponseEntity<Void> deleteSecret(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        Secret secret = secretsService.findById(id);

        if (!secret.userId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        secretsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
