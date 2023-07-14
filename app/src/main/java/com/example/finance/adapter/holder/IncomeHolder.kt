package com.example.finance.adapter.holder

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finance.R
import com.example.finance.database.model.FinanceModel
import com.example.finance.ui.income.EditIncomeNotesActivity
import java.text.NumberFormat
import java.util.Locale

class IncomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvMoney = itemView.findViewById<TextView>(R.id.tv_money)
    val tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)
    val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    val ctx = itemView.context

    fun setData(model : FinanceModel) {
        // currency format
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val formattedNominal = formatter.format(model.nominal)
        tvMoney.text = formattedNominal

        tvDesc.text = model.desc
        tvDate.text = model.date
        //menambahkan fungsi ketika rootview dari textview nama di klik
        tvMoney.rootView.setOnClickListener {
            val go = Intent(ctx, EditIncomeNotesActivity::class.java)
            go.putExtra("income data", model)
            ctx.startActivity(go)
        }
    }
}
