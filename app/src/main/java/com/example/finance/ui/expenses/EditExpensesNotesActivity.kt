package com.example.finance.ui.expenses

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.finance.R
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.database.model.FinanceModel
import com.example.finance.databinding.ActivityEditExpensesNotesBinding
import com.google.android.material.chip.Chip
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditExpensesNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditExpensesNotesBinding
    private var selectedChip: String? = null

    // currency format
    private var rawNumericValue: Long = 0
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No need to do anything here
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // No need to do anything here
        }

        override fun afterTextChanged(s: Editable?) {
            // Remove the old currency format to get the raw value
            val rawValue = s.toString().replace("[^\\d]".toRegex(), "")

            // Store the raw numeric value in cents
            rawNumericValue = rawValue.toLong()

            // Format the raw value to currency format
            val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

            val formattedValue = currencyFormatter.format(rawNumericValue / 100)

            // Set the formatted value back to the EditText
            binding.edtMoney.removeTextChangedListener(this)
            binding.edtMoney.setText(formattedValue)
            binding.edtMoney.setSelection(formattedValue.length - 3)
            binding.edtMoney.addTextChangedListener(this)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditExpensesNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        // to bring up the previous data
        val data = intent.getParcelableExtra("expenses data") ?: FinanceModel()

        // Set currency format for edtMoney
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        // Convert data.nominal to raw numeric value in cents
        rawNumericValue = (data.nominal * 100).toLong()

        // Format the raw numeric value to currency format
        val formattedValue = currencyFormatter.format(rawNumericValue / 100)

        // Set the formatted value to the EditText
        binding.edtMoney.setText(formattedValue)
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
            if (rawNumericValue != 0L) {
                data.nominal = (rawNumericValue / 100).toInt()
            } else {
                Toast.makeText(this@EditExpensesNotesActivity, "Harap isi data nominal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

        // Add the TextWatcher to the edtMoney EditText
        binding.edtMoney.addTextChangedListener(textWatcher)
    }

    // retrieve date in real-time
    private fun getCurrentDateTime():String {
        val sdf = SimpleDateFormat("HH:mm dd MMMM yyyy", Locale("id"))
        return sdf.format(Date())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}