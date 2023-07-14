package com.example.finance.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finance.database.model.FinanceModel

@Dao
interface FinanceDao {
    @Query("SELECT * FROM financemodel ORDER BY id DESC")
    fun getAll(): List<FinanceModel?>?

    @Query("SELECT COALESCE(SUM(CASE WHEN type = 'Pemasukan' THEN nominal ELSE 0 END), 0) - COALESCE(SUM(CASE WHEN type = 'Pengeluaran' THEN nominal ELSE 0 END), 0) FROM financemodel")
    fun getTotalNominal(): Int

    @Query("SELECT SUM(nominal) FROM financemodel WHERE type = 'Pengeluaran'")
    fun getTotalExpenses(): Int

    @Query("SELECT SUM(nominal) FROM financemodel WHERE type = 'Pemasukan'")
    fun getTotalIncome(): Int

    @Query("SELECT * FROM financemodel WHERE type = 'Pengeluaran' ORDER BY id DESC")
    fun getAllExpenses(): List<FinanceModel>?

    @Query("SELECT * FROM financemodel WHERE type = 'Pemasukan' ORDER BY id DESC")
    fun getAllIncome(): List<FinanceModel>?

    @Insert
    fun insert(vararg financeModels: FinanceModel)

    @Update
    fun update(vararg financeModels: FinanceModel)

    @Delete
    fun delete(vararg financeModels: FinanceModel)
}
