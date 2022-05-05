package com.flybits.conciergesample.viewholders

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.flybits.conciergesample.R

class ViewHolder2(v: View) : RecyclerView.ViewHolder(v) {
    var frameLayout: FrameLayout = v.findViewById(R.id.embeded_concierge_recycler)
}