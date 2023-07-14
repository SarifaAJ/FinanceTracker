package com.example.finance.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finance.R
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.database.model.FinanceModel
import com.example.finance.databinding.ActivityAddNotesBinding
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddNotesBinding
    private var selectedChip: String? = null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        // save button
        binding.saveBtn.setOnClickListener {
            val data = FinanceModel()
            data.nominal = binding.edtMoney.text.toString().toInt()
            data.desc = binding.edtDesc.text.toString()
            data.date = getCurrentTime()
            data.type = selectedChip!! // Set jenis data sebagai "Pengeluaran"

            val financeDao = db.financeDao()
            if (data.type == "Pengeluaran") {
                financeDao?.insert(data) // Insert data ke tabel pengeluaran
            } else if (data.type == "Pemasukan") {
                financeDao?.insert(data) // Insert data ke tabel pemasukan
            } // Menggunakan metode insert yang mencakup pemasukan dan pengeluaran

            Toast.makeText(this@AddNotesActivity, "Catatan pengeluaran berhasil ditambahkan", Toast.LENGTH_SHORT).show()

            this@AddNotesActivity.finish()
        }

        // Chip Group Listener
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val chip: Chip? = findViewById(checkedId)
            chip?.let {
                selectedChip = when (it.id) {
                    R.id.chip_pengeluaran -> "Pengeluaran"
                    R.id.chip_pemasukan -> "Pemasukan"
                    else -> null
                }
            }
        }
    }

    // retrieve date in real-time
    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id"))
        return sdf.format(Date())
    }
}