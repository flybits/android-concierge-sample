package com.flybits.conciergesample.fragment

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.conciergesample.R
import com.flybits.concierge.FlybitsConcierge
import com.flybits.concierge.enums.ShowMode
import com.flybits.context.ReservedContextPlugin
import com.flybits.context.plugins.FlybitsContextPlugin
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_account.*
import java.util.concurrent.TimeUnit

const val LOCATION_PERMISSION_REQUEST = 123

class AccountFragment: Fragment() {

    companion object {
        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }

    private var concierge: FlybitsConcierge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        concierge = FlybitsConcierge.with(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { requestPermissions(it) }

        text_picked_for_you.setOnClickListener {
            concierge?.show(ShowMode.NEW_ACTIVITY)
        }

        text_logout.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            concierge?.logOut(object: BasicResultCallback{
                override fun onException(exception: FlybitsException) {
                    context?.let { _ ->
                        progressBar.visibility = View.GONE
                        Snackbar.make(it, "Error logging out!", Snackbar.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess() {
                    context?.let { _ ->
                        progressBar.visibility = View.GONE
                        Snackbar.make(it, "Logged out!", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            val plugin = FlybitsContextPlugin.Builder(ReservedContextPlugin.LOCATION)
                .setRefreshTime(10, 5, TimeUnit.MINUTES)
                .build()
            Log.d("AccountFragment", "Starting context plugin")
            FlybitsConcierge.with(context).startContextPlugin(plugin)
        }
    }

    private fun requestPermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)) {

                MaterialDialog.Builder(activity)
                    .title("Location Permission")
                    .positiveText("Ok")
                    .content("Location helps us deliver content to you when it is most relevant. Enable the location permission to take advantage of this feature.")
                    .dismissListener {
                        ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
                    }
                    .build()
                    .show()

            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
            }
        }
    }
}