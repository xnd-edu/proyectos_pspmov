package org.example.apilogin.ui.security.crypto;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Servicio para encriptación asimétrica con RSA y EC
 * Usa par de claves: pública (cifrar/verificar) y privada (descifrar/firmar)
 */
@Service
public class AsymmetricEncryptionService {

    private static final String RSA_ALGORITHM = "RSA";
    private static final int RSA_KEY_SIZE = 2048;
    private static final int AES_KEY_SIZE = 256;
    private static final int IV_SIZE = 12;

    /**
     * Genera un par de claves RSA
     */
    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(RSA_KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Convierte clave pública a Base64
     */
    public String publicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * Convierte clave privada a Base64
     */
    public String privateKeyToBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * Convierte Base64 a clave pública
     * Detecta automáticamente si es RSA o EC
     */
    public PublicKey base64ToPublicKey(String base64PublicKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Convierte Base64 a clave privada
     * Detecta automáticamente si es RSA o EC
     */
    public PrivateKey base64ToPrivateKey(String base64PrivateKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    public PrivateKey binaryToPrivateKey(byte[] privateKeyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA con OAEP Padding (más seguro)
     * OAEP = Optimal Asymmetric Encryption Padding
     * Tamaño máximo de datos: (keySize / 8) - 42 bytes
     * Para RSA-2048: 256 - 42 = 214 bytes
     */
    public String encryptOAEP(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decryptOAEP(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * Firma digital con RSA
     * Se firma con la clave privada y se verifica con la pública
     */
    public byte[] sign(String message, PrivateKey privateKey) throws Exception {
        String signatureAlgorithm = "SHA256withRSA";

        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    /**
     * Verifica una firma digital con RSA
     */
    public boolean verify(String message, String signatureBase64, PublicKey publicKey) throws Exception {
        String signatureAlgorithm = "SHA256withRSA";

        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
        return signature.verify(signatureBytes);
    }

    /**
     * Encriptación híbrida: Combina RSA + AES
     * 1. Genera clave AES aleatoria
     * 2. Encripta datos con AES (rápido)
     * 3. Encripta clave AES con RSA (seguro)
     *
     * Ventaja: Permite encriptar mensajes grandes sin límite de tamaño RSA
     */
    public HybridEncryptionResult encryptHybrid(String plainText, PublicKey publicKey) throws Exception {
        // Generar clave AES aleatoria
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);
        Key aesKey = keyGenerator.generateKey();

        // Encriptar datos con AES-GCM
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[IV_SIZE]; // GCM usa IV de 12 bytes
        new SecureRandom().nextBytes(iv);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, new javax.crypto.spec.GCMParameterSpec(128, iv));
        byte[] encryptedData = aesCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Encriptar clave AES con RSA
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(aesKey.getEncoded());

        return new HybridEncryptionResult(
                Base64.getEncoder().encodeToString(encryptedData),
                Base64.getEncoder().encodeToString(encryptedKey),
                Base64.getEncoder().encodeToString(iv)
        );
    }

    public String decryptHybrid(HybridEncryptionResult hybridResult, PrivateKey privateKey) throws Exception {
        // Descifrar clave AES con RSA
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(Base64.getDecoder().decode(hybridResult.encryptedKey()));
        Key aesKey = new javax.crypto.spec.SecretKeySpec(aesKeyBytes, "AES");

        // Descifrar datos con AES-GCM
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = Base64.getDecoder().decode(hybridResult.iv());
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, new javax.crypto.spec.GCMParameterSpec(128, iv));
        byte[] decryptedData = aesCipher.doFinal(Base64.getDecoder().decode(hybridResult.encryptedData()));

        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * Record para almacenar resultado de encriptación híbrida
     */
    public record HybridEncryptionResult(String encryptedData, String encryptedKey, String iv) {}

    /**
     * Record para almacenar resultado de ECIES
     * ephemeralPublicKey: Clave pública efímera generada para este cifrado
     * encryptedData: Datos cifrados con AES-GCM
     * iv: Vector de inicialización para AES-GCM
     */
    public record ECIESResult(String ephemeralPublicKey, String encryptedData, String iv) {}
}

