package com.example.finance.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.finance.R
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.database.model.FinanceModel
import com.example.finance.databinding.ActivityAddNotesBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddNotesBinding

    private var rawNumericValue: Long = 0

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        // Set data.date with the current time
        binding.tvDate.text = getCurrentTimeShowFormat()

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
                Toast.makeText(this@AddNotesActivity, "Please fill out the nominal data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            data.desc = binding.edtDesc.text.toString()
            data.date = getCurrentTime()

            val selectedType = binding.typeDropdown.text.toString()

            if (selectedType.isEmpty()) {
                Toast.makeText(this@AddNotesActivity, "Please select the data type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                data.type = if (selectedType == "Income") {
                    "Pemasukan"
                } else {
                    "Pengeluaran"
                }
            }

            val financeDao = db.financeDao()

            if (data.desc.isEmpty()) {
                Toast.makeText(this@AddNotesActivity, "Please complete all data", Toast.LENGTH_SHORT).show()
            } else {
                financeDao?.insert(data)
                Toast.makeText(this@AddNotesActivity, "Expense record successfully added", Toast.LENGTH_SHORT).show()
                this@AddNotesActivity.finish()
            }
        }

        // dropdown
        val type = resources.getStringArray(R.array.type_drop_down)
        val typeAdapter = ArrayAdapter(this, R.layout.dropdown_item, type)
        binding.typeDropdown.setAdapter(typeAdapter)

    }

    // retrieve date in real-time
    private fun getCurrentTimeShowFormat(): String {
        val sdf = SimpleDateFormat("HH:mm dd MMMM yyyy", Locale("id"))
        return sdf.format(Date())
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale("id"))
        return sdf.format(Date())
    }
}