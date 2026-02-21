package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.SharedSecret;
import org.example.apilogin.domain.service.SharedSecretService;
import org.example.apilogin.ui.dto.ShareSecretDTO;
import org.example.apilogin.ui.dto.SharedSecretResponse;
import org.example.apilogin.ui.security.crypto.SymmetricEncryptionService;
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
}
