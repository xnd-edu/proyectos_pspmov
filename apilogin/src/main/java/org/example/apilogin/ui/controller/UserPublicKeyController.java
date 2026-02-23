package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.UserPublicKey;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UserPublicKeyService;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.UserPublicKeyResponse;
import org.example.apilogin.ui.security.crypto.SymmetricEncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constantes.API_PUBLIC_KEYS)
public class UserPublicKeyController {
    private final UserPublicKeyService userPublicKeyService;
    private final UsuarioService usuarioService;
    private final SymmetricEncryptionService symmetricEncryptionService;

    public UserPublicKeyController(UserPublicKeyService userPublicKeyService, UsuarioService usuarioService, SymmetricEncryptionService symmetricEncryptionService) {
        this.userPublicKeyService = userPublicKeyService;
        this.usuarioService = usuarioService;
        this.symmetricEncryptionService = symmetricEncryptionService;
    }

    @GetMapping(Constantes.API_BY_USERNAME)
    public ResponseEntity<UserPublicKeyResponse> getPublicKeyOfUserByUsername(@PathVariable String username) {
        Usuario usuario = usuarioService.findByUsername(username);

        UserPublicKey userPublicKey = userPublicKeyService.findByUserId(usuario.id());
        String publicKeyBase64 = symmetricEncryptionService.bytesToBase64(userPublicKey.publicKey());
        String serverSignatureBase64 = symmetricEncryptionService.bytesToBase64(userPublicKey.serverSignature());

        return ResponseEntity.ok(new UserPublicKeyResponse(userPublicKey.id(), userPublicKey.userId(), publicKeyBase64, serverSignatureBase64, userPublicKey.createdAt()));
    }
}
