package org.example.apilogin.ui.security.crypto;

import org.example.apilogin.common.Constantes;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Servicio para encriptación asimétrica con RSA
 * Usa par de claves: pública (cifrar/verificar) y privada (descifrar/firmar)
 */
@Service
public class AsymmetricEncryptionService {

    /**
     * Genera un par de claves RSA
     */
    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Constantes.RSA_ALGORITHM);
        keyPairGenerator.initialize(Constantes.RSA_KEY_SIZE);
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
     * Detecta automáticamente si es RSA
     */
    public PublicKey base64ToPublicKey(String base64PublicKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(Constantes.RSA_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Convierte Base64 a clave privada
     * Detecta automáticamente si es RSA
     */
    public PrivateKey base64ToPrivateKey(String base64PrivateKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(Constantes.RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    public PrivateKey binaryToPrivateKey(byte[] privateKeyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(Constantes.RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA con OAEP Padding (más seguro)
     * OAEP = Optimal Asymmetric Encryption Padding
     * Tamaño máximo de datos: (keySize / 8) - 42 bytes
     * Para RSA-2048: 256 - 42 = 214 bytes
     */
    public String encryptOAEP(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constantes.OAEP_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decryptOAEP(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Constantes.OAEP_PADDING);
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
        String signatureAlgorithm = Constantes.SIGNATURE_ALGORITHM;

        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    /**
     * Verifica una firma digital con RSA
     */
    public boolean verify(String message, String signatureBase64, PublicKey publicKey) throws Exception {
        String signatureAlgorithm = Constantes.SIGNATURE_ALGORITHM;

        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
        return signature.verify(signatureBytes);
    }
}
