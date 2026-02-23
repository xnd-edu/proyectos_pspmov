package com.example.composeapp.data.security

import android.util.Base64
import com.example.composeapp.common.CryptoConstants
import com.example.composeapp.common.ErrorMessages
import com.example.composeapp.domain.model.AesEncryptionResult
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class SymmetricCryptoManager {

    /**
     * Genera una clave AES de 256 bits totalmente aleatoria.
     * Se usa para el cifrado de un secreto nuevo (Data Key).
     */
    fun generateRandomKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(CryptoConstants.AES_KEY_SIZE)
        return keyGenerator.generateKey()
    }

    /**
     * Genera una clave AES derivada de contraseña (PBKDF2)
     * Coincide con: PBKDF2WithHmacSHA256 del servidor
     */
    fun generateKeyFromPassword(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance(CryptoConstants.PBKDF2_ALGORITHM)
        val spec = PBEKeySpec(
            password.toCharArray(),
            salt,
            CryptoConstants.PBKDF2_ITERATIONS,
            CryptoConstants.AES_KEY_SIZE
        )
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, CryptoConstants.AES_ALGORITHM)
    }

    /**
     * Encripta con AES-GCM
     * Retorna el resultado con el texto cifrado y el IV, ambos codificados en Base64 para facilitar su manejo.
     */
    fun encryptGCM(plainText: String, key: SecretKey): AesEncryptionResult {
        val cipher = Cipher.getInstance(CryptoConstants.TRANSFORMATION)
        val iv = ByteArray(CryptoConstants.IV_SIZE).apply { SecureRandom().nextBytes(this) }

        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(CryptoConstants.GCM_TAG_LENGTH, iv))
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))

        return AesEncryptionResult(
            encryptedData = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP),
            iv = Base64.encodeToString(iv, Base64.NO_WRAP)
        )
    }

    /**
     * Desencripta AES-GCM usando el IV y los datos por separado.
     */
    fun decryptGCM(encryptedBytes: ByteArray, key: SecretKey, iv: ByteArray): Result<String> {
        return try {
            val cipher = Cipher.getInstance(CryptoConstants.TRANSFORMATION)

            val spec = GCMParameterSpec(CryptoConstants.GCM_TAG_LENGTH, iv)

            cipher.init(Cipher.DECRYPT_MODE, key, spec)

            val decryptedBytes = cipher.doFinal(encryptedBytes)
            Result.success(String(decryptedBytes, Charsets.UTF_8))
        } catch (e: AEADBadTagException) {
            // Este error ocurre específicamente cuando la contraseña/llave es incorrecta
            Result.failure(Exception(ErrorMessages.PASSWORD_NOT_VALID))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun generateSalt(): ByteArray {
        val salt = ByteArray(CryptoConstants.SALT_SIZE)
        SecureRandom().nextBytes(salt)
        return salt
    }

    fun bytesToBase64(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun base64ToBytes(base64: String): ByteArray {
        return Base64.decode(base64, Base64.NO_WRAP)
    }
}