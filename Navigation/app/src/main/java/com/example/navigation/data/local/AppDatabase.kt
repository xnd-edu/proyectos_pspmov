package com.example.navigation.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.navigation.data.local.dao.CocheConductorDao
import com.example.navigation.data.local.dao.CochesDao
import com.example.navigation.data.local.dao.ConductoresDao
import com.example.navigation.data.local.entities.CocheEntity
import com.example.navigation.data.local.entities.ConductorEntity
import com.example.navigation.data.local.entities.CocheConductorCrossRef

@Database(
    entities = [CocheEntity::class, ConductorEntity::class, CocheConductorCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cochesDao(): CochesDao
    abstract fun conductoresDao(): ConductoresDao
    abstract fun cocheConductorDao(): CocheConductorDao
}