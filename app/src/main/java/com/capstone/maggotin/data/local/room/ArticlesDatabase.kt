package com.capstone.maggotin.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.maggotin.data.local.entity.ArticlesEntity

@Database(entities = [ArticlesEntity::class], version = 1, exportSchema = false)
abstract class ArticlesDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao

    companion object {
        @Volatile
        private var instance: ArticlesDatabase? = null

        fun getInstance(context: Context): ArticlesDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ArticlesDatabase::class.java,
                    "Articles.db"
                ).build()
            }
    }
}