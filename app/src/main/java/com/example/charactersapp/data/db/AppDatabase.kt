package com.example.charactersapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.charactersapp.data.dao.CharactersDao
import com.example.charactersapp.data.dao.RemoteKeysDao
import com.example.charactersapp.data.entity.RemoteKeys
import com.example.charactersapp.models.Character

@Database(entities = [Character::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract val charactersDao: CharactersDao
    abstract val remoteKeysDao: RemoteKeysDao

    //Room should only be initiated once, marked volatile to be thread safe.
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildDatabase(
                    context
                ).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}