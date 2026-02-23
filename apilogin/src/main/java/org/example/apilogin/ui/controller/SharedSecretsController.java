package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.SharedSecret;
import org.example.apilogin.domain.service.SharedSecretService;
import org.example.apilogin.ui.dto.ShareSecretDTO;
import org.example.apilogin.ui.dto.SharedSecretResponse;
import org.example.apilogin.ui.security.crypto.SymmetricEncryptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.API_SECRETS_SHARE)
public class SharedSecretsController {
    private final SharedSecretService sharedSecretService;
    private final SymmetricEncryptionService symmetricEncryptionService;

    public SharedSecretsController(SharedSecretService sharedSecretService, SymmetricEncryptionService symmetricEncryptionService) {
        this.sharedSecretService = sharedSecretService;
        this.symmetricEncryptionService = symmetricEncryptionService;
    }

    @GetMapping
    public ResponseEntity<List<SharedSecret>> getSharedSecrets(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(sharedSecretService.findBySharedWithId(userId));
    }

    @GetMapping(Constantes.API_BY_ID)
    public ResponseEntity<SharedSecret> getSharedSecret(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        SharedSecret sharedSecret = sharedSecretService.findById(id);

        // Verifica que el usuario sea el propietario o el destinatario
        if (!sharedSecret.sharedWithId().equals(userId) && !sharedSecret.ownerId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(sharedSecret);
    }

    @PostMapping
    public ResponseEntity<SharedSecretResponse> shareSecret(Authentication authentication, @RequestBody ShareSecretDTO shareSecretDTO) {
        Long userId = Long.parseLong(authentication.getName());
        SharedSecret sharedSecret = new SharedSecret(
                null,
                shareSecretDTO.secretId(),
                userId,
                shareSecretDTO.sharedWithUserId(),
                symmetricEncryptionService.base64ToBytes(shareSecretDTO.encryptedData()),
                symmetricEncryptionService.base64ToBytes(shareSecretDTO.encryptedKey()),
                symmetricEncryptionService.base64ToBytes(shareSecretDTO.iv()),
                null
        );
        sharedSecret = sharedSecretService.save(sharedSecret);
        return ResponseEntity.ok(new SharedSecretResponse(sharedSecret.id(), sharedSecret.sharedWithId(), sharedSecret.createdAt()));
    }

    @DeleteMapping(Constantes.API_BY_ID)
    public ResponseEntity<Void> revokeSharedSecretBySecret(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        List<SharedSecret> sharedSecrets = sharedSecretService.findBySecretId(id);

        boolean isOwner = sharedSecrets.stream().anyMatch(s -> s.ownerId().equals(userId));
        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        sharedSecrets.stream()
                .filter(s -> s.ownerId().equals(userId))
                .forEach(s -> sharedSecretService.delete(s.id()));

        return ResponseEntity.noContent().build();
    }
}
