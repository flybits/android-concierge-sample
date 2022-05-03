package com.flybits.conciergesample.fragment

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.Concierge
import com.flybits.concierge.ConciergeConnectCallBack
import com.flybits.concierge.OptIn2PhaseCallback
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import com.flybits.context.ReservedContextPlugin
import com.flybits.context.plugins.FlybitsContextPlugin
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_account.*
import java.util.concurrent.TimeUnit

const val LOCATION_PERMISSION_REQUEST = 123

class AccountFragment : Fragment() {

    var is2Phase = false

    companion object {
        fun newInstance(): AccountFragment {
            return AccountFragment()
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

        is2Phase = LoginFragment.is2Phase

        if (is2Phase) {
            text_logout.setText("Go Back")
            insights.visibility = View.GONE
            insights2.visibility = View.VISIBLE
            text_picked_for_you.visibility = View.GONE
            text_picked_for_you2.visibility = View.VISIBLE
            text_picked_for_you3.visibility = View.VISIBLE
        } else {
            text_logout.text = "Logout"
            insights.visibility = View.VISIBLE
            insights2.visibility = View.GONE
            text_picked_for_you.visibility = View.VISIBLE
            text_picked_for_you2.visibility = View.GONE
            text_picked_for_you3.visibility = View.GONE
        }

        if (is2Phase && Concierge.isConnected(requireContext())!!) {
            text_logout.text = "Logout"
        }

        // load the concierge in embedded
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        Concierge.fragment(
            requireContext(),
            Container.Expose,
            null,
            arrayListOf(
                ConciergeOptions.DisplayNavigation,
                ConciergeOptions.Settings,
                ConciergeOptions.Notifications
            )
        ).let {
            transaction?.replace(R.id.embeded_concierge, it)
        }
        transaction?.commit()

        text_logout.setOnClickListener {
            if (!LoginFragment.is2Phase) {
                progressBar.visibility = View.VISIBLE

                // Call disconnect on Concierge.
                Concierge.disconnect(requireContext(), object : BasicResultCallback {
                    override fun onException(exception: FlybitsException) {
                        context?.let { _ ->
                            progressBar.visibility = View.GONE
                            Snackbar.make(it, "Error logging out!", Snackbar.LENGTH_LONG).show()
                        }
                    }

                    override fun onSuccess() {
                        context?.let { _ ->
                            LoginFragment.username = ""
                            LoginFragment.password = ""
                            progressBar.visibility = View.GONE
                            Snackbar.make(it, "Logged out!", Snackbar.LENGTH_SHORT).show()
                            //   findNavController().navigate(R.id.loginFragment)
                        }
                    }
                })
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

            val plugin = FlybitsContextPlugin.Builder(ReservedContextPlugin.LOCATION)
                .setRefreshTime(10, 5, TimeUnit.MINUTES)
                .build()
            Log.d("AccountFragment", "Starting context plugin")
        }
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
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                Concierge.fragment(
                    requireContext(),
                    Container.Expose,
                    null,
                    arrayListOf(
                        ConciergeOptions.DisplayNavigation,
                        ConciergeOptions.Settings,
                        ConciergeOptions.Notifications
                    )
                ).let {
                    transaction?.replace(R.id.embeded_concierge, it)
                }
                transaction?.commit()
            } else {
                view?.let { Snackbar.make(it, "Already connected", Snackbar.LENGTH_LONG).show() }
            }

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
    }
}