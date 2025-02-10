package com.eurico.patol.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eurico.patol.model.database.ContentDTO
import com.eurico.patol.model.database.MaterialDTO

@Database(entities = [MaterialDTO::class, ContentDTO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun materialDao(): MaterialDao
}