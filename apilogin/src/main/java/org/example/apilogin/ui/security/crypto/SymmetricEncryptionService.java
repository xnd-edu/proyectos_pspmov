package org.example.apilogin.ui.security.crypto;

import org.example.apilogin.common.Constantes;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Servicio para encriptación simétrica con AES
 * Implementa diferentes modos de operación: ECB, CBC, CTR, GCM
 */
@Service
public class SymmetricEncryptionService {

    private static final String ALGORITHM = "AES";
    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_TAG_LENGTH = 128;

    private static final int PBKDF2_ITERATIONS = 65536; // Iteraciones para PBKDF2

    /**
     * Genera una clave AES de 256 bits
     */
    public SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE);
        return keyGenerator.generateKey();
    }

    /**
     * Genera una clave AES desde un password con salt personalizado
     *
     * @param password String base para generar la clave
     * @param salt Salt único (mínimo 8 bytes recomendado)
     * @return SecretKey derivada del password y salt
     */
    public SecretKey generateKeyFromPassword(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            PBKDF2_ITERATIONS,
                AES_KEY_SIZE
        );
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }

    /**
     * Convierte un array de bytes a Base64 (genérico para clave, IV, etc.)
     */
    public String bytesToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Convierte un Base64 a array de bytes (genérico para clave, IV, etc.)
     */
    public byte[] base64ToBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    /**
     * AES-GCM: Galois/Counter Mode
     * RECOMENDADO - Proporciona encriptación y autenticación (AEAD)
     * Detecta modificaciones en el texto cifrado
     */
    public String encryptGCM(String plainText, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Concatenar IV + texto cifrado (que incluye el tag de autenticación)
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public String decryptGCM(String encryptedText, SecretKey key) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);

        byte[] iv = new byte[Constantes.IV_SIZE];
        byte[] encrypted = new byte[combined.length - Constantes.IV_SIZE];
        System.arraycopy(combined, 0, iv, 0, Constantes.IV_SIZE);
        System.arraycopy(combined, Constantes.IV_SIZE, encrypted, 0, encrypted.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * Genera un IV (Vector de Inicialización) aleatorio
     */
    public byte[] generateIV() {
        byte[] iv = new byte[Constantes.IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

}
