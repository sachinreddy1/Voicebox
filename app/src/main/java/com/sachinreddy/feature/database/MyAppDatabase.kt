package com.sachinreddy.feature.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sachinreddy.feature.data.User

private const val DATABASE = "data"

@Database(
    entities = [User::class],
    version = 2,
    exportSchema = false
)
abstract class MyAppDatabase : RoomDatabase() {

    abstract fun MyDao(): MyDao

    companion object {
        @Volatile
        private var instance: MyAppDatabase? = null

        fun getInstance(context: Context): MyAppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        fun buildDatabase(context: Context): MyAppDatabase {
            return Room.databaseBuilder(
                context, MyAppDatabase::class.java,
                DATABASE
            )
                // Need to change this
                .allowMainThreadQueries()
                .build()
        }
    }
}