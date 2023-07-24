package com.example.finance.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.finance.R
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.database.model.FinanceModel
import com.example.finance.databinding.ActivityAddNotesBinding
import com.google.android.material.chip.Chip
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddNotesBinding
    private var selectedChip: String? = null

    private var rawNumericValue: Long = 0

    // calendar
    private val calendar: Calendar = Calendar.getInstance()
    private var selectedYear: Int = calendar.get(Calendar.YEAR)
    private var selectedMonth: Int = calendar.get(Calendar.MONTH)


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        // Set data.date dengan waktu saat ini
        binding.date.text = getCurrentTime()

        // Set currency format for edtMoney
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        binding.edtMoney.addTextChangedListener(object : TextWatcher {
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
                val formattedValue = currencyFormatter.format(rawNumericValue / 100)

                // Set the formatted value back to the EditText
                binding.edtMoney.removeTextChangedListener(this)
                binding.edtMoney.setText(formattedValue)
                binding.edtMoney.setSelection(formattedValue.length - 3)
                binding.edtMoney.addTextChangedListener(this)
            }
        })

        // save button
        binding.saveBtn.setOnClickListener {
            val data = FinanceModel()
            if (rawNumericValue != 0L) {
                data.nominal = (rawNumericValue / 100).toInt()
            } else {
                Toast.makeText(this@AddNotesActivity, "Harap isi data nominal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            data.desc = binding.edtDesc.text.toString()
            data.date = binding.date.text.toString()

            if (selectedChip.isNullOrEmpty()) {
                Toast.makeText(this@AddNotesActivity, "Harap pilih jenis data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                data.type = selectedChip!!
            }

            val financeDao = db.financeDao()

            if (data.desc.isEmpty()) {
                Toast.makeText(this@AddNotesActivity, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            } else {
                if (data.type == "Pengeluaran") {
                    financeDao?.insert(data) // Insert data ke tabel pengeluaran
                } else if (data.type == "Pemasukan") {
                    financeDao?.insert(data) // Insert data ke tabel pemasukan
                } // Menggunakan metode insert yang mencakup pemasukan dan pengeluaran

                Toast.makeText(this@AddNotesActivity, "Catatan pengeluaran berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                this@AddNotesActivity.finish()
            }
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
        val sdf = SimpleDateFormat("HH:mm dd MMMM yyyy", Locale("id"))
        return sdf.format(Date())
    }

    private fun getFormattedDate(year: Int, month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)

        val simpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }
}