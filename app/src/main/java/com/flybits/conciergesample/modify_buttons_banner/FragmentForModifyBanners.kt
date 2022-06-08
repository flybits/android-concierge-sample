package com.flybits.conciergesample.banners

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.Concierge
import com.flybits.conciergesample.R
import com.flybits.conciergesample.modify_buttons_banner.ComplexRecyclerViewModifyButtonsAdapter
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_account.*

class FragmentForModifyBanners : Fragment() {
    companion object {
        fun newInstance(): FragmentForBanners {
            return FragmentForBanners()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { requestPermissions(it) }
        setHasOptionsMenu(true)

        // Recycler view
        val adapter =
            context?.let { ComplexRecyclerViewModifyButtonsAdapter(getSampleArrayList(), it) }
        rv_common_items.adapter = adapter
        rv_common_items.layoutManager = LinearLayoutManager(context)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
    }


    private fun requestPermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    ACCESS_FINE_LOCATION
                )
            ) {

                MaterialDialog.Builder(activity)
                    .title("Location Permission")
                    .positiveText("Ok")
                    .content("Location helps us deliver content to you when it is most relevant. Enable the location permission to take advantage of this feature.")
                    .dismissListener {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION_REQUEST
                        )
                    }
                    .build()
                    .show()

            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login_flybits) {
            // Check if connected already.
            if (!Concierge.isConnected(requireContext())) {
                // Call Connect and load the fragment.
                Concierge.connect(requireContext(), AnonymousConciergeIDP())
                rv_common_items.adapter?.notifyDataSetChanged()
            } else {
                Concierge.disconnect(requireContext(), object : BasicResultCallback {
                    override fun onException(exception: FlybitsException) {
                        context?.let { _ ->
                            progressBar.visibility = View.GONE
                            view?.let {
                                Snackbar.make(
                                    it,
                                    "Error logging out!",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    override fun onSuccess() {
                        context?.let { _ ->
                            progressBar.visibility = View.GONE
                            view?.let {
                                Snackbar.make(it, "Logged out!", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
    }


    private fun getSampleArrayList(): ArrayList<Any> {
        val items: ArrayList<Any> = ArrayList()
        items.add(Savings("$4,000", "$5,000", "10,000"))
        items.add(Investments("RRSP", "$5000"))
        items.add(Investments("TFSA", "$10,000"))
        items.add("Concierge")
        items.add(Investments("GIC", "$15,5000"))
        return items
    }


    data class Investments(val type: String, var amount: String)

    data class Savings(
        val amount_cheq: String,
        var amount_savings: String,
        var amount_credit: String
    )
}