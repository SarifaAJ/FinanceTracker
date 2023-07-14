package com.example.finance.adapter.holder

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finance.R
import com.example.finance.database.model.FinanceModel
import com.example.finance.ui.expenses.EditExpensesNotesActivity

class ExpensesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvMoney = itemView.findViewById<TextView>(R.id.tv_money)
    val tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)
    val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    val ctx = itemView.context

    fun setData(model : FinanceModel) {
        tvMoney.text = model.nominal.toString()
        tvDesc.text = model.desc
        tvDate.text = model.date
        //menambahkan fungsi ketika rootview dari textview nama di klik
        tvMoney.rootView.setOnClickListener {
            val go = Intent(ctx, EditExpensesNotesActivity::class.java)
            go.putExtra("expenses data", model)
            ctx.startActivity(go)
        }
    }
}