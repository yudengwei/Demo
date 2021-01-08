package com.abiao.libjetpackdemo.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.abiao.libjetpackdemo.app.App
import com.abiao.libjetpackdemo.app.Constant
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.workmanager.GetDataWork
import com.example.base.delegate.PreferenceDelegate

@Database(entities = [Shoe::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoeDao() : ShoeDao

    companion object {

        private var isFirst by PreferenceDelegate(App.getApplicationContext(), Constant.SP_FIRST, true)

        private var instance : AppDatabase? = null

        fun getInstance() : AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: getDatabase()
            }
        }

        private fun getDatabase() : AppDatabase {
            return Room.databaseBuilder(App.getApplicationContext(), AppDatabase::class.java, "jet_demo")
                .allowMainThreadQueries()
                .addCallback(callback)
                .build()
        }

        private val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                if (isFirst) {
                    WorkManager.getInstance(App.getApplicationContext())
                        .enqueue(OneTimeWorkRequest.from(GetDataWork::class.java))
                }
            }
        }
    }
}