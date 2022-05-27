package com.flybits.conciergesample.banners

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flybits.conciergesample.R


class ViewHolderBanners3(v: View) : RecyclerView.ViewHolder(v) {
    var amount_chequing: TextView = v.findViewById(R.id.amount_chequing)
    var amount_savings: TextView = v.findViewById(R.id.amount_savings)
    var amount_credit: TextView = v.findViewById(R.id.amount_credit)
}