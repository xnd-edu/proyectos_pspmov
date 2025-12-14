package com.example.navigation.data.remote

import com.example.navigation.BuildConfig
import com.example.navigation.data.remote.api.IGDBApi
import com.example.navigation.data.remote.api.JsonPlaceholderApi
import com.example.navigation.data.remote.igdb.IgdbAuthInterceptor
import com.example.navigation.data.remote.igdb.TwitchAuthApi
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
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideJsonPlaceholderApi(retrofit: Retrofit): JsonPlaceholderApi {
        return retrofit.create(JsonPlaceholderApi::class.java)
    }

    // === IGDB / Twitch Configuration ===

    @Provides
    @Singleton
    @Named("TwitchAuth")
    fun provideTwitchAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://id.twitch.tv/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTwitchAuthApi(
        @Named("TwitchAuth") retrofit: Retrofit
    ): TwitchAuthApi {
        return retrofit.create(TwitchAuthApi::class.java)
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
    @Named("IgdbOkHttp")
    fun provideIgdbOkHttpClient(
        igdbAuthInterceptor: IgdbAuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(igdbAuthInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("Igdb")
    fun provideIgdbRetrofit(
        @Named("IgdbOkHttp") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.igdb.com/v4/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIgdbApi(
        @Named("Igdb") retrofit: Retrofit
    ): IGDBApi {
        return retrofit.create(IGDBApi::class.java)
    }
}