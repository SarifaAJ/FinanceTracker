package com.example.finance.adapter.holder

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finance.R
import com.example.finance.database.model.FinanceModel
import com.example.finance.ui.expenses.EditExpensesNotesActivity
import java.text.NumberFormat
import java.util.Locale

class ExpensesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvMoney = itemView.findViewById<TextView>(R.id.tv_money)
    private val tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    private val ctx = itemView.context

    fun setData(model : FinanceModel) {
        // currency format
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val formattedNominal = formatter.format(model.nominal)
        tvMoney.text = formattedNominal

        tvDesc.text = model.desc
        tvDate.text = model.date
        // add a function when the rootview of the name textview is clicked
        tvMoney.rootView.setOnClickListener {
            val go = Intent(ctx, EditExpensesNotesActivity::class.java)
            go.putExtra("expenses data", model)
            ctx.startActivity(go)
        }
    }
}
