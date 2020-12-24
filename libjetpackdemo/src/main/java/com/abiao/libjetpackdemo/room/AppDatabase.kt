package com.abiao.libjetpackdemo.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.abiao.libjetpackdemo.app.App
import com.abiao.libjetpackdemo.model.Shoe

@Database(entities = [Shoe::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoeDao() : ShoeDao

    companion object {

        private var instance : AppDatabase? = null

        fun getInstance() : AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: getDatabase()
            }
        }

        private fun getDatabase() : AppDatabase {
            return Room.databaseBuilder(App.getApplicationContext(), AppDatabase::class.java, "jet_demo")
                .allowMainThreadQueries()
                .build()
        }
    }
}