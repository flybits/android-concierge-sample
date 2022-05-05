package com.flybits.conciergesample.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flybits.conciergesample.R


class ViewHolder1(v: View) : RecyclerView.ViewHolder(v) {
    var label1: TextView = v.findViewById(R.id.text1)
    var label2: TextView = v.findViewById(R.id.text2)
}