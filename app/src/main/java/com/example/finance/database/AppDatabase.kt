package com.example.finance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finance.database.converters.Converters
import com.example.finance.database.dao.FinanceDao
import com.example.finance.database.model.FinanceModel

@Database(entities = [FinanceModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun financeDao() : FinanceDao?
}