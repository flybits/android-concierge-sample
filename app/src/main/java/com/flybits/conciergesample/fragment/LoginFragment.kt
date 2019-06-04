package com.flybits.conciergesample.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.flybits.conciergesample.R
import com.flybits.commons.library.api.idps.AnonymousIDP
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.AuthenticationStatusListener
import com.flybits.concierge.FlybitsConcierge
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: Fragment(), AuthenticationStatusListener {

    private var concierge: FlybitsConcierge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginFragment", "Created login fragment")
        super.onCreate(savedInstanceState)
        concierge = FlybitsConcierge.with(context)
        if (concierge?.isAuthenticated == true) {
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        concierge?.registerAuthenticationStateListener(this)

        button_login.setOnClickListener {
            Log.d("LoginFragment", "Login pressed")
            progressBar?.visibility = View.VISIBLE
            concierge?.authenticate(AnonymousIDP())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        concierge?.unregisterAuthenticationStateListener(this)
    }

    override fun onAuthenticationStarted() {
        Log.d("LoginFragment", "onAuthenticationStarted")
    }

    override fun onAuthenticated() {
        Log.d("LoginFragment", "onAuthenticated")
        progressBar?.visibility = View.GONE
        view?.let {
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
        }
    }

    override fun onAuthenticationError(e: FlybitsException?) {
        Log.d("LoginFragment", "onAuthenticationError")
        progressBar?.visibility = View.GONE
        view?.let {
            Snackbar.make(it, "Error authenticating", Snackbar.LENGTH_LONG).show()
        }

    }
}