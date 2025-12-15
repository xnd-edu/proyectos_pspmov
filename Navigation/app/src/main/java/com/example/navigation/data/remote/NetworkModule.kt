package com.example.navigation.data.remote

import com.example.navigation.BuildConfig
import com.example.navigation.common.ApiConstants
import com.example.navigation.common.DaggerNames
import com.example.navigation.data.remote.api.IGDBApi
import com.example.navigation.data.remote.api.JsonPlaceholderApi
import com.example.navigation.data.remote.igdb.IgdbAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.JSON_PLACEHOLDER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideJsonPlaceholderApi(retrofit: Retrofit): JsonPlaceholderApi {
        return retrofit.create(JsonPlaceholderApi::class.java)
    }

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