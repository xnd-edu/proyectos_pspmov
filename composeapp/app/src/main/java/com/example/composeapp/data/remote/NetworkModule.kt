package com.example.composeapp.data.remote

import com.example.composeapp.BuildConfig
import com.example.composeapp.common.ApiConstants
import com.example.composeapp.common.DaggerNames
import com.example.composeapp.data.remote.api.AuthApi
import com.example.composeapp.data.remote.api.IGDBApi
import com.example.composeapp.data.remote.api.ReindeerApi
import com.example.composeapp.data.remote.interceptors.AuthInterceptor
import com.example.composeapp.data.remote.interceptors.IgdbAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.REINDEER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideReindeerApi(retrofit: Retrofit): ReindeerApi {
        return retrofit.create(ReindeerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    // El IGDB requiere un Retrofit y OkHtttp separado porque tiene una autenticación diferente (client ID y access token)
    @Provides
    @Singleton
    @Named(DaggerNames.TWITCH_AUTH)
    fun provideTwitchAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.TWITCH_AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIgdbAuthInterceptor(): IgdbAuthInterceptor {
        return IgdbAuthInterceptor(
            clientId = BuildConfig.TWITCH_CLIENT_ID,
            accessToken = BuildConfig.IGDB_ACCESS_TOKEN
        )
    }

    @Provides
    @Singleton
    @Named(DaggerNames.IGDB_OKHTTP)
    fun provideIgdbOkHttpClient(
        igdbAuthInterceptor: IgdbAuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(igdbAuthInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named(DaggerNames.IGDB)
    fun provideIgdbRetrofit(
        @Named(DaggerNames.IGDB_OKHTTP) okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.IGDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIgdbApi(
        @Named(DaggerNames.IGDB) retrofit: Retrofit
    ): IGDBApi {
        return retrofit.create(IGDBApi::class.java)
    }
}