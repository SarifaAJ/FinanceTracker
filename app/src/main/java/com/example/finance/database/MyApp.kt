package com.example.finance.database

import android.app.Application
import androidx.room.Room
import com.example.finance.database.dao.FinanceDao

class MyApp : Application() {
    companion object {
        lateinit var db: AppDatabase
        lateinit var financeDao: FinanceDao
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "finance"
        ).allowMainThreadQueries().build()
        financeDao = db.financeDao()!!
    }
}