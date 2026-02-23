package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
import org.example.apilogin.domain.model.Secret;
import org.example.apilogin.domain.model.SharedSecret;
import org.example.apilogin.domain.model.UserPublicKey;
import org.example.apilogin.domain.service.SecretsService;
import org.example.apilogin.domain.service.SharedSecretService;
import org.example.apilogin.domain.service.UserPublicKeyService;
import org.example.apilogin.ui.dto.SecretCreateResponse;
import org.example.apilogin.ui.dto.SharedSecretResponse;
import org.example.apilogin.ui.dto.SimCreateSecretDTO;
import org.example.apilogin.ui.dto.SimDecryptSecretDTO;
import org.example.apilogin.ui.dto.SimDecryptSecretResponse;
import org.example.apilogin.ui.dto.SimShareSecretDTO;
import org.example.apilogin.ui.security.crypto.AsymmetricEncryptionService;
import org.example.apilogin.ui.security.crypto.SymmetricEncryptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Controller de SIMULACIÓN para pruebas HTTP del cifrado punto a punto.
 * En la app real el cifrado/descifrado ocurre en el cliente Android.
 * Aquí se hace en el servidor únicamente para poder testear los flujos.
 */
@RestController
@RequestMapping(Constantes.API_SIMULATION)
public class SimulationController {

    private final SecretsService secretsService;
    private final SharedSecretService sharedSecretService;
    private final UserPublicKeyService userPublicKeyService;
    private final SymmetricEncryptionService symmetricService;
    private final AsymmetricEncryptionService asymmetricService;

    public SimulationController(SecretsService secretsService,
                                SharedSecretService sharedSecretService,
                                UserPublicKeyService userPublicKeyService,
                                SymmetricEncryptionService symmetricService,
                                AsymmetricEncryptionService asymmetricService) {
        this.secretsService = secretsService;
        this.sharedSecretService = sharedSecretService;
        this.userPublicKeyService = userPublicKeyService;
        this.symmetricService = symmetricService;
        this.asymmetricService = asymmetricService;
    }

    /**
     * CASO DE USO 1: El usuario crea un secreto.
     * Simula lo que haría el cliente Android:
     * 1. Genera un salt aleatorio
     * 2. Deriva una clave AES-256 a partir de la contraseña del usuario + salt (PBKDF2)
     * 3. Cifra el texto plano con AES-GCM
     * 4. Guarda (encryptedData, iv, salt) en vault_secrets — igual que POST /api/secrets
     * Devuelve SecretResponse(id, createdAt) — igual que el endpoint real.
     */
    @PostMapping(Constantes.API_SIMULATION_CREATE_SECRET)
    public ResponseEntity<SecretCreateResponse> createSecret(
            @RequestBody SimCreateSecretDTO request,
            Authentication authentication) {
        try {
            Long userId = Long.parseLong(authentication.getName());

            // 1. Generar salt aleatorio
            byte[] salt = new byte[Constantes.SALT_SIZE];
            new SecureRandom().nextBytes(salt);

            // 2. Derivar clave AES desde la contraseña del usuario + salt (PBKDF2)
            SecretKey key = symmetricService.generateKeyFromPassword(request.password(), salt);

            // 3. Generar IV y cifrar con AES-GCM
            // encryptGCM devuelve Base64(iv + ciphertext) — extraemos solo el ciphertext
            byte[] iv = symmetricService.generateIV();
            String combined64 = symmetricService.encryptGCM(request.plainText(), key, iv);
            byte[] combined = Base64.getDecoder().decode(combined64);
            byte[] ciphertext = new byte[combined.length - Constantes.IV_SIZE];
            System.arraycopy(combined, Constantes.IV_SIZE, ciphertext, 0, ciphertext.length);

            // 4. Guardar en BD — igual que POST /api/secrets
            Secret saved = secretsService.save(new Secret(null, userId, ciphertext, iv, salt, null));

            return ResponseEntity.ok(new SecretCreateResponse(saved.id(), saved.createdAt()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * CASO DE USO 2: El usuario comparte un secreto con otro usuario.
     * Simula lo que haría el cliente Android:
     * 1. Recupera el secreto cifrado de la BD
     * 2. Descifra con la contraseña del propietario (PBKDF2 + AES-GCM)
     * 3. Genera una clave AES aleatoria nueva
     * 4. Cifra el texto plano con esa clave AES (AES-GCM)
     * 5. Obtiene la clave pública RSA del receptor via GET /api/public-keys/{id}
     * 6. Cifra la clave AES con la clave pública del receptor (RSA-OAEP)
     * 7. Guarda en shared_secrets — igual que POST /api/secrets/share
     * Devuelve SharedSecretResponse(id, sharedWithId, createdAt) — igual que el endpoint real.
     */
    @PostMapping(Constantes.API_SIMULATION_SHARE_SECRET)
    public ResponseEntity<SharedSecretResponse> shareSecret(
            @RequestBody SimShareSecretDTO request,
            Authentication authentication) {
        try {
            Long ownerId = Long.parseLong(authentication.getName());

            // 1. Recuperar secreto original del propietario
            Secret secret;
            try {
                secret = secretsService.findById(request.secretId());
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 2. Descifrar el secreto con la contraseña del propietario (PBKDF2 + AES-GCM)
            // Reconstruir combined (iv + ciphertext) tal como espera decryptGCM
            SecretKey ownerKey = symmetricService.generateKeyFromPassword(request.ownerPassword(), secret.salt());
            byte[] combined = new byte[secret.iv().length + secret.encryptedData().length];
            System.arraycopy(secret.iv(), 0, combined, 0, secret.iv().length);
            System.arraycopy(secret.encryptedData(), 0, combined, secret.iv().length, secret.encryptedData().length);
            String plainText = symmetricService.decryptGCM(symmetricService.bytesToBase64(combined), ownerKey);

            // 3. Obtener clave pública RSA del receptor — igual que GET /api/public-keys/{id}
            UserPublicKey recipientKey = userPublicKeyService.findByUserId(request.recipientUserId());

            // Construir el mensaje que el servidor firmó
            String messageToVerify = String.format(Constantes.SERVER_SIGNATURE_MESSAGE,
                    recipientKey.userId(),
                    Base64.getEncoder().encodeToString(recipientKey.publicKey()),
                    recipientKey.createdAt());

            // El cliente obtiene de su código la clave publica del servidor (hardcodeada por seguridad) para verificar la firma
            PublicKey serverPublicKey = asymmetricService.base64ToPublicKey(request.serverPublicKeyBase64());

            // Verificar la firma
            boolean valid = asymmetricService.verify(
                    messageToVerify,
                    Base64.getEncoder().encodeToString(recipientKey.serverSignature()),
                    serverPublicKey);

            if (!valid) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // 4. Generar clave AES aleatoria nueva para el secreto compartido
            SecretKey sharedAesKey = symmetricService.generateKey();

            // 5. Cifrar el texto plano con la nueva clave AES-GCM
            byte[] sharedIv = symmetricService.generateIV();
            String sharedCombined64 = symmetricService.encryptGCM(plainText, sharedAesKey, sharedIv);
            byte[] sharedCombined = Base64.getDecoder().decode(sharedCombined64);
            byte[] sharedCiphertext = new byte[sharedCombined.length - Constantes.IV_SIZE];
            System.arraycopy(sharedCombined, Constantes.IV_SIZE, sharedCiphertext, 0, sharedCiphertext.length);

            // 6. Cifrar la clave AES con la clave pública RSA del receptor (OAEP)
            PublicKey recipientRsaKey = asymmetricService.base64ToPublicKey(
                    symmetricService.bytesToBase64(recipientKey.publicKey())
            );
            byte[] encryptedKey = Base64.getDecoder().decode(
                    asymmetricService.encryptOAEP(
                            symmetricService.bytesToBase64(sharedAesKey.getEncoded()),
                            recipientRsaKey
                    )
            );

            // 7. Guardar en shared_secrets — igual que POST /api/secrets/share
            SharedSecret saved = sharedSecretService.save(new SharedSecret(
                    null,
                    secret.id(),
                    ownerId,
                    request.recipientUserId(),
                    sharedCiphertext,
                    encryptedKey,
                    sharedIv,
                    null
            ));

            return ResponseEntity.ok(new SharedSecretResponse(saved.id(), saved.sharedWithId(), saved.createdAt()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint de simulación: Descifra un secreto guardado usando la contraseña del usuario.
     * Equivalente a GET /api/secrets/{id} pero devolviendo el texto plano.
     */
    @PostMapping(Constantes.API_DECRYPT_SECRET)
    public ResponseEntity<SimDecryptSecretResponse> decryptSecret(
            @PathVariable Long id,
            @RequestBody SimDecryptSecretDTO request,
            Authentication authentication) {
        try {
            Long userId = Long.parseLong(authentication.getName());
            Secret secret = secretsService.findById(id);
            if (!secret.userId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            // Derivar clave AES desde la contraseña y el salt guardado
            SecretKey key = symmetricService.generateKeyFromPassword(request.password(), secret.salt());
            // Reconstruir combined (iv + encryptedData)
            byte[] combined = new byte[secret.iv().length + secret.encryptedData().length];
            System.arraycopy(secret.iv(), 0, combined, 0, secret.iv().length);
            System.arraycopy(secret.encryptedData(), 0, combined, secret.iv().length, secret.encryptedData().length);
            String plainText = symmetricService.decryptGCM(symmetricService.bytesToBase64(combined), key);
            return ResponseEntity.ok(new SimDecryptSecretResponse(plainText));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
