package com.flybits.conciergesample.banners

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.flybits.conciergesample.R

class ViewHolderBanners2(v: View) : RecyclerView.ViewHolder(v) {
    var frameLayout: FrameLayout = v.findViewById(R.id.embeded_concierge_recycler)
}