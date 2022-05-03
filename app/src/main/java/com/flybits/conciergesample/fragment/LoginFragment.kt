package com.flybits.conciergesample.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.flybits.commons.library.api.FlybitsConfiguration
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.AuthenticationStatusListener
import com.flybits.concierge.Concierge
import com.flybits.concierge.FlybitsConciergeConfiguration
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import com.flybits.conciergesample.activity.DemoAppCompatActivityActionBar
import com.flybits.context.ContextManager
import com.flybits.context.ReservedContextPlugin
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), AuthenticationStatusListener {

    // initialize variables
    var textView: TextView? = null
    lateinit var selectedLanguage: BooleanArray
    var langList: ArrayList<Int> = ArrayList()
    private var langArray =
        arrayOf("DisplayNavigation", "Settings", "Notifications", "ShowNotifications", "Horizontal")

    companion object {
        var is2Phase = false
        var username = ""
        var password = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginFragment", "Created login fragment")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun changeVisibility(view: Int) {
        button_WithNotificationsContent.visibility = view
        button_WithDefault.visibility = view
        button_WithCategories.visibility = view
        button_WithExpose.visibility = view
        button_WithNavigationBar.visibility = view
        button_logout.visibility = view
    }

    private fun changeLogin(value: Int) {
        when (value) {
            1 -> {
                button_login_anonymous?.apply {
                    isEnabled = false
                    text = "You Are Now Connected"
                }
                project_id.visibility = View.GONE
                gateway_url_spinner.visibility = View.GONE
                btn_set_project_id.visibility = View.GONE
            }
            0 -> {
                button_login_anonymous?.apply {
                    isEnabled = true
                    text = "Anonymous Login"
                }
                project_id.visibility = View.VISIBLE
                gateway_url_spinner.visibility = View.VISIBLE
                btn_set_project_id.visibility = View.VISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeVisibility(View.GONE)
        if (Concierge.isConnected(requireContext())) {
            changeVisibility(View.VISIBLE)
            changeLogin(1)
        } else {
            changeLogin(0)
        }
        Concierge.registerAuthenticationStateListener(this)

        btn_set_project_id.setOnClickListener {
            if (project_id.text.isNotBlank() && gateway_url_spinner.text.isNotBlank()) {
                val builder = FlybitsConciergeConfiguration.Builder(requireContext())
                    .setProjectId(project_id.text.toString())
                    .setGateWayUrl(gateway_url_spinner.text.toString())
                    .build()

                Concierge.configure(
                    builder, arrayListOf(
                        ContextManager.PluginType.ReservedPlugin(
                            ReservedContextPlugin
                                .LOCATION
                        )
                    ), requireContext()
                )
                Snackbar.make(
                    it,
                    "Cannot customize it for now, please change the JSON, resetting it for now",
                    Snackbar.LENGTH_LONG
                ).show()
                project_id.setText(FlybitsConfiguration.Builder.projectId)
                gateway_url_spinner.setText(FlybitsConfiguration.Builder.gatewayUrl)
            } else {
                Snackbar.make(
                    it,
                    "Cannot have project id or url empty, defaulting to Json Configuration",
                    Snackbar.LENGTH_LONG
                ).show()
                project_id.setText(FlybitsConfiguration.Builder.projectId)
                gateway_url_spinner.setText(FlybitsConfiguration.Builder.gatewayUrl)
            }
        }

        project_id.setText(FlybitsConfiguration.Builder.projectId)
        gateway_url_spinner.setText(FlybitsConfiguration.Builder.gatewayUrl)

        button_login_anonymous.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            is2Phase = false
            Concierge.connect(requireContext(), AnonymousConciergeIDP())
        }

        button_WithNavigationBar.setOnClickListener {
            startActivity(Intent(requireContext(), DemoAppCompatActivityActionBar::class.java))
        }

        button_WithDefault.setOnClickListener {
            val bundle = bundleOf(
                "ContainerType" to Container.None,
                "settings" to true,
                "notification" to true,
                "displaynavigation" to true
            )
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment, bundle)
        }

        button_WithCategories.setOnClickListener {
            val bundle = bundleOf(
                "ContainerType" to Container.Categories,
                "settings" to true,
                "notification" to true,
                "displaynavigation" to true
            )
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment, bundle)
        }

        button_WithExpose.setOnClickListener {
            val bundle = bundleOf(
                "ContainerType" to Container.Expose,
                "settings" to true,
                "notification" to true,
                "displaynavigation" to true
            )
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment, bundle)
        }

        button_WithNotificationsContent.setOnClickListener {
            val bundle = bundleOf(
                "ContainerType" to Container.None,
                "settings" to true,
                "notification" to true,
                "displaynavigation" to true,
                "showNotificationsApi" to true
            )
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment, bundle)
        }

        button_logout.setOnClickListener {
            Concierge.disconnect(requireContext(), object : BasicResultCallback {
                override fun onException(exception: FlybitsException) {
                    context?.let { _ ->
                        progressBar.visibility = View.GONE
                        Snackbar.make(it, "Error logging out!", Snackbar.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess() {
                    context?.let { _ ->
                        changeVisibility(View.GONE)
                        changeLogin(0)
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Concierge.unregisterAuthenticationStateListener(this)
    }

    override fun onAuthenticationStarted() {
        Log.d("LoginFragment", "onAuthenticationStarted")
    }

    override fun onAuthenticated() {
        Log.d("LoginFragment", "onAuthenticated")
        progressBar?.visibility = View.GONE
        changeVisibility(View.VISIBLE)
        changeLogin(1)
    }

    override fun onAuthenticationError(e: FlybitsException?) {
        Log.d("LoginFragment", "onAuthenticationError")
        progressBar?.visibility = View.GONE
        changeVisibility(View.GONE)
        changeLogin(0)
        view?.let {
            Snackbar.make(it, "Error authenticating", Snackbar.LENGTH_LONG).show()
        }
    }
}