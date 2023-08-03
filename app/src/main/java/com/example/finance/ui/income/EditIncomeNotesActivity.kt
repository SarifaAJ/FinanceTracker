package com.example.finance.ui.income

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.finance.R
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.database.model.FinanceModel
import com.example.finance.databinding.ActivityEditIncomeNotesBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditIncomeNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditIncomeNotesBinding

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
        binding = ActivityEditIncomeNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        // to bring up the previous data
        val data = intent.getParcelableExtra("income data") ?: FinanceModel()

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

        // dropdown
        val type = resources.getStringArray(R.array.type_drop_down)
        val typeAdapter = ArrayAdapter(this, R.layout.dropdown_item, type)
        binding.typeDropdown.setAdapter(typeAdapter)

        // Set the initial selection of the dropdown based on the previously saved data.type
        val selectedTypeIndex = if (data.type == "Pengeluaran") {
            1 // "Expense" in the dropdown
        } else {
            0 // "Income" in the dropdown
        }
        binding.typeDropdown.setText(type[selectedTypeIndex], false)

        binding.saveBtn.setOnClickListener {
            if (rawNumericValue != 0L) {
                data.nominal = (rawNumericValue / 100).toInt()
            } else {
                Toast.makeText(this@EditIncomeNotesActivity, "Please fill out the nominal data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            data.desc = binding.edtDesc.text.toString()
            data.date = getCurrentTime()

            // Get the selected type from the dropdown
            val selectedType = binding.typeDropdown.text.toString()
            data.type = if (selectedType == "Income") {
                "Pemasukan"
            } else {
                "Pengeluaran"
            }
            val financeDao = db.financeDao()
            if (data.type == "Pengeluaran") {
                financeDao?.update(data) // Update data in the expenses table
            } else if (data.type == "Pemasukan") {
                financeDao?.update(data) // Update data in the income table
            }

            Toast.makeText(this@EditIncomeNotesActivity, "Note updated successfully", Toast.LENGTH_SHORT).show()
            this@EditIncomeNotesActivity.finish()
        }

        binding.btnDelete.setOnClickListener {
            val financeDao = db.financeDao()
            if (data.type == "Pengeluaran") {
                financeDao?.delete(data) // Delete data in the expenses table
            } else if (data.type == "Pemasukan") {
                financeDao?.delete(data) // Delete data in the income table
            }

            Toast.makeText(this@EditIncomeNotesActivity, "Note successfully deleted", Toast.LENGTH_SHORT).show()
            this@EditIncomeNotesActivity.finish()
        }

        // Add the TextWatcher to the edtMoney EditText
        binding.edtMoney.addTextChangedListener(textWatcher)
    }

    // retrieve date in real-time
    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale("id"))
        return sdf.format(Date())
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}