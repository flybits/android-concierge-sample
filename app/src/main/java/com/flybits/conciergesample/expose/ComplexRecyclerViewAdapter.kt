package com.flybits.conciergesample.expose

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R

class ComplexRecyclerViewAdapter(private val items: List<Any>, var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val Investment = 0
    private val Concierge = 1
    private val Savings = 2

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is FragmentForExpose.Investments) {
            return Investment
        } else if (items[position] is String) {
            return Concierge
        } else if (items[position] is FragmentForExpose.Savings) {
            return Savings
        }
        return -1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)
        viewHolder = when (viewType) {
            Investment -> {
                val v1: View = inflater.inflate(R.layout.layout_viewholder1, viewGroup, false)
                ViewHolder1(v1)
            }
            Concierge -> {
                val v2: View = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false)
                ViewHolder2(v2)
            }
            Savings -> {
                val v2: View = inflater.inflate(R.layout.layout_viewholder3, viewGroup, false)
                ViewHolder3(v2)
            }
            else -> {
                val v2: View = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false)
                ViewHolder2(v2)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            Investment -> {
                val vh1 = viewHolder as ViewHolder1
                configureViewHolder1(vh1, position)
            }
            Concierge -> {
                val vh2 = viewHolder as ViewHolder2
                configureViewHolder2(vh2, position)
            }
            Savings -> {
                val vh3 = viewHolder as ViewHolder3
                configureViewHolder3(vh3, position)
            }
            else -> {

            }
        }
    }

    private fun configureViewHolder1(vh1: ViewHolder1, position: Int) {
        val investments = items[position] as FragmentForExpose.Investments
        vh1.label1.text = "Type: ${investments.type}"
        vh1.label2.text = "Amount: ${investments.amount}"
    }

    private fun configureViewHolder2(vh2: ViewHolder2, position: Int) {
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        com.flybits.concierge.Concierge.fragment(
            context,
            Container.Expose,
            null,
            arrayListOf(
                ConciergeOptions.DisplayNavigation(),
                ConciergeOptions.Settings,
                ConciergeOptions.Notifications
            )
        ).let {
            transaction.replace(vh2.frameLayout.id, it)
        }
        transaction.commit()
    }

    private fun configureViewHolder3(vh3: ViewHolder3, position: Int) {
        val savings = items[position] as FragmentForExpose.Savings
        vh3.amount_chequing.text = savings.amount_cheq
        vh3.amount_credit.text = savings.amount_credit
        vh3.amount_savings.text = savings.amount_savings
    }
}