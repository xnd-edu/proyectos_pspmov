package com.example.composeapp.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.composeapp.common.CryptoConstants
import com.example.composeapp.common.ErrorMessages
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class AsymmetricCryptoManager {

    /**
     * Genera el par de claves DENTRO del hardware de seguridad de Android.
     * La clave privada nunca sale del dispositivo.
     */
    fun generateAndStoreKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            CryptoConstants.ANDROID_KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            CryptoConstants.KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setKeySize(CryptoConstants.RSA_KEY_SIZE)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            .build()

        keyPairGenerator.initialize(spec)
        return keyPairGenerator.generateKeyPair()
    }

    /**
     * Obtener la clave pública del Keystore (para enviarla al servidor)
     */
    fun getMyPublicKey(): PublicKey? {
        val keyStore = KeyStore.getInstance(CryptoConstants.ANDROID_KEYSTORE).apply { load(null) }
        val entry = keyStore.getEntry(CryptoConstants.KEY_ALIAS, null) as? KeyStore.PrivateKeyEntry
        return entry?.certificate?.publicKey
    }

    /**
     * Obtener la clave privada (referencia) del Keystore
     */
    fun getMyPrivateKey(): PrivateKey? {
        val keyStore = KeyStore.getInstance(CryptoConstants.ANDROID_KEYSTORE).apply { load(null) }
        val entry = keyStore.getEntry(CryptoConstants.KEY_ALIAS, null) as? KeyStore.PrivateKeyEntry
        return entry?.privateKey
    }

    /**
     * Convierte String Base64 del servidor a PublicKey
     */
    fun base64ToPublicKey(base64Key: String): PublicKey {
        val decoded = Base64.decode(base64Key, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(decoded)
        return KeyFactory.getInstance(CryptoConstants.RSA_ALGORITHM).generatePublic(spec)
    }

    fun publicKeyToBase64(publicKey: PublicKey): String {
        return Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
    }

    // --- ENCRIPTADO HÍBRIDO (Parte RSA) ---

    /**
     * Encriptar clave AES (u otros datos cortos) con RSA-OAEP
     * Usado para enviar la llave a otro usuario.
     */
    fun encryptOAEP(plainText: ByteArray, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance(CryptoConstants.TRANSFORMATION_OAEP)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(plainText)
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    /**
     * Desencriptar clave AES con mi clave privada del Keystore
     */
    fun decryptOAEP(encryptedBase64: String): ByteArray {
        val privateKey = getMyPrivateKey() ?: throw IllegalStateException(ErrorMessages.NO_KEY_PAIR)

        val cipher = Cipher.getInstance(CryptoConstants.TRANSFORMATION_OAEP)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)
        return cipher.doFinal(encryptedBytes)
    }

    // --- FIRMA DIGITAL ---

    /**
     * Verificar firma del servidor
     */
    fun verifySignature(message: String, signatureBase64: String, serverPublicKey: PublicKey): Boolean {
        val signature = Signature.getInstance(CryptoConstants.SIGNATURE_ALGORITHM)
        signature.initVerify(serverPublicKey)
        signature.update(message.toByteArray(StandardCharsets.UTF_8))

        val signatureBytes = Base64.decode(signatureBase64, Base64.DEFAULT)
        return signature.verify(signatureBytes)
    }
}