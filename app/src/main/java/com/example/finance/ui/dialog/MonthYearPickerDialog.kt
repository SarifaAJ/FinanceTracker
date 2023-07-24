package com.example.finance.ui.dialog

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.NumberPicker
import com.example.finance.R
import java.util.Calendar

class MonthYearPickerDialog(
    context: Context,
    private val listener: OnDateSetListener,
    private val calendar: Calendar
) : Dialog(context) {

    private var monthPicker: NumberPicker
    private var yearPicker: NumberPicker

    init {
        setContentView(R.layout.dialog_month_year_picker)
        setTitle(R.string.select_month_year)

        monthPicker = findViewById(R.id.monthPicker)
        yearPicker = findViewById(R.id.yearPicker)

        // Set up the month picker
        val monthValues = context.resources.getStringArray(R.array.month)
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.displayedValues = monthValues
        monthPicker.value = calendar.get(Calendar.MONTH) + 1 // Month is 0-based

        // Set up the year picker
        val currentYear = calendar.get(Calendar.YEAR)
        yearPicker.minValue = 2000 // Change this value to set the minimum year
        yearPicker.maxValue = currentYear
        yearPicker.value = currentYear

        // Set up the buttons
        val cancelBtn = findViewById<Button>(R.id.btnCancel)
        cancelBtn.setOnClickListener { dismiss() }

        val okBtn = findViewById<Button>(R.id.btnOk)
        okBtn.setOnClickListener {
            val selectedYear = yearPicker.value
            val selectedMonth = monthPicker.value - 1 // Convert back to 0-based index

            calendar.set(Calendar.YEAR, selectedYear)
            calendar.set(Calendar.MONTH, selectedMonth)

            listener.onDateSet(selectedYear, selectedMonth)
            dismiss()
        }
    }

    interface OnDateSetListener {
        fun onDateSet(year: Int, month: Int)
    }
}
