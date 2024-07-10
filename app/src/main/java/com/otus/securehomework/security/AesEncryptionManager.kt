package com.otus.securehomework.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class AesEncryptionManager @Inject constructor(private val aesSecretKeyProvider: AesKeyProvider) {

    private val encryptionAlgorithm = "AES/GCM/NoPadding"

    private fun getInitializationVector(): GCMParameterSpec {
        return GCMParameterSpec(128, FIXED_IV)
    }

    fun encryptAes(plainText: String): String {
        val cipher = Cipher.getInstance(encryptionAlgorithm)
        cipher.init(Cipher.ENCRYPT_MODE, aesSecretKeyProvider.getAesSecretKey(), getInitializationVector())
        val encodedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encodedBytes, Base64.NO_WRAP)
    }

    fun decryptAes(encrypted: String): String {
        val cipher = Cipher.getInstance(encryptionAlgorithm)
        cipher.init(Cipher.DECRYPT_MODE, aesSecretKeyProvider.getAesSecretKey(), getInitializationVector())
        val decodedBytes = Base64.decode(encrypted, Base64.NO_WRAP)
        val decoded = cipher.doFinal(decodedBytes)
        return String(decoded, Charsets.UTF_8)
    }

    companion object {
        private val FIXED_IV = byteArrayOf(55, 54, 53, 52, 51, 50, 49, 48, 47, 46, 45, 44)
    }
}
