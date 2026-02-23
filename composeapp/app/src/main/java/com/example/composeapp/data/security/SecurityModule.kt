package com.example.composeapp.data.security

import com.example.composeapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    @Provides
    @Singleton
    fun provideSymmetricCryptoManager(): SymmetricCryptoManager {
        return SymmetricCryptoManager()
    }

    @Provides
    @Singleton
    fun provideAsymmetricCryptoManager(): AsymmetricCryptoManager {
        return AsymmetricCryptoManager()
    }

    @Provides
    @Singleton
    fun provideServerVerifier(asymmetricManager: AsymmetricCryptoManager): ServerVerifier {
        val serverPubKey = asymmetricManager.base64ToPublicKey(BuildConfig.SERVER_PUBLIC_KEY)
        return ServerVerifier(asymmetricManager, serverPubKey)
    }
}