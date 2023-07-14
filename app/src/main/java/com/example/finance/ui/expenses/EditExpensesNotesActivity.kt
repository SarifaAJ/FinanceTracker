package com.example.finance.ui.expenses

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.finance.R
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.database.model.FinanceModel
import com.example.finance.databinding.ActivityEditExpensesNotesBinding
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditExpensesNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditExpensesNotesBinding
    private var selectedChip: String? = null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditExpensesNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        val data = intent.getParcelableExtra("expenses data") ?: FinanceModel()
        Log.e("expenses data", "${data.nominal}.")

        binding.edtMoney.setText(data.nominal.toString())
        binding.edtDesc.setText(data.desc)
        binding.tvDate.setText(data.date)

        // Chip Group Listener
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val chip = findViewById<Chip>(checkedId)
            chip?.let {
                selectedChip = when (it.id) {
                    R.id.chip_pengeluaran -> "Pengeluaran"
                    R.id.chip_pemasukan -> "Pemasukan"
                    else -> null
                }
            }
        }

        // Set selected chip
        if (data.type == "Pengeluaran") {
            binding.chipGroup.check(R.id.chip_pengeluaran)
        } else if (data.type == "Pemasukan") {
            binding.chipGroup.check(R.id.chip_pemasukan)
        }

        binding.saveBtn.setOnClickListener {
            data.nominal = binding.edtMoney.text.toString().toInt()
            data.desc = binding.edtDesc.text.toString()
            data.date = getCurrentDateTime()
            data.type = selectedChip ?: ""

            val financeDao = db.financeDao()
            if (data.type == "Pengeluaran") {
                financeDao?.update(data) // Update data pada tabel pengeluaran
            } else if (data.type == "Pemasukan") {
                financeDao?.update(data) // Update data pada tabel pemasukan
            }

            Toast.makeText(this@EditExpensesNotesActivity, "Catatan berhasil diperbarui", Toast.LENGTH_SHORT).show()
            this@EditExpensesNotesActivity.finish()
        }

        binding.btnDelete.setOnClickListener {
            val financeDao = db.financeDao()
            if (data.type == "Pengeluaran") {
                financeDao?.delete(data) // Hapus data pada tabel pengeluaran
            } else if (data.type == "Pemasukan") {
                financeDao?.delete(data) // Hapus data pada tabel pemasukan
            }

            Toast.makeText(this@EditExpensesNotesActivity, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show()
            this@EditExpensesNotesActivity.finish()
        }
    }

    // retrieve date in real-time
    private fun getCurrentDateTime():String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id"))
        return sdf.format(Date())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
