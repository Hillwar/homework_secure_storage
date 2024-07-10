package com.otus.securehomework.security

import javax.crypto.SecretKey
import javax.inject.Inject

class AesKeyProviderImplLegacy
@Inject constructor(
    private val rsaKeyManager: RsaKeyManager
) : AesKeyProvider() {

    override fun getAesSecretKey(): SecretKey {
        return rsaKeyManager.getOrGenerateAesSecretKey()
    }
}