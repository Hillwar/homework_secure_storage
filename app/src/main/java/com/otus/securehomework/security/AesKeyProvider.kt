package com.otus.securehomework.security

import java.security.KeyStore
import javax.crypto.SecretKey

abstract class AesKeyProvider {
    protected val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEY_PROVIDER).apply {
            load(null)
        }
    }

    abstract fun getAesSecretKey(): SecretKey

    companion object {
        const val KEY_PROVIDER = "AndroidKeyStore"
    }
}
