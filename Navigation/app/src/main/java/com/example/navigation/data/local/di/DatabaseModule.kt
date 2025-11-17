package com.example.navigation.data.local.di

import android.content.Context
import androidx.room.Room
import com.example.navigation.data.local.AppDatabase
import com.example.navigation.data.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .createFromAsset(Constants.DATABASE_ASSET_PATH)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCocheDao(database: AppDatabase) = database.cochesDao()

    @Provides
    fun provideConductorDao(database: AppDatabase) = database.conductoresDao()

    @Provides
    fun provideCocheConductorDao(database: AppDatabase) = database.cocheConductorDao()
}