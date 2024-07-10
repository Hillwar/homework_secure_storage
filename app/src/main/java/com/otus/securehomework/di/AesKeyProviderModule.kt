package com.otus.securehomework.di

import android.content.Context
import android.os.Build
import com.otus.securehomework.security.AesKeyProvider
import com.otus.securehomework.security.AesKeyProviderImpl
import com.otus.securehomework.security.AesKeyProviderImplLegacy
import com.otus.securehomework.security.RsaKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AesKeyProviderModule {
    @Provides
    @Singleton
    fun providesAesSecretKeyProvider(@ApplicationContext context: Context): AesKeyProvider {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AesKeyProviderImpl()
        } else {
            val rsaKeyManager = RsaKeyManager(context)
            AesKeyProviderImplLegacy(rsaKeyManager)
        }
    }

    @Provides
    @Singleton
    fun providesAesSecretKeyProviderImplLegacy(
        @ApplicationContext context: Context,
        rsaKeyManager: RsaKeyManager
    ): AesKeyProviderImplLegacy {
        return AesKeyProviderImplLegacy(rsaKeyManager)
    }
}
