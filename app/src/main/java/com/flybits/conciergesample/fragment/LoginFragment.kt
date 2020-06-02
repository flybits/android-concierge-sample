package com.flybits.conciergesample.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.flybits.commons.library.api.idps.AnonymousIDP
import com.flybits.commons.library.api.idps.JwtIDP
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.concierge.AuthenticationStatusListener
import com.flybits.concierge.FlybitsConcierge
import com.flybits.conciergesample.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), AuthenticationStatusListener {

    private var concierge: FlybitsConcierge? = null

    companion object {
        var is2Phase = false
        var username = ""
        var password = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginFragment", "Created login fragment")
        super.onCreate(savedInstanceState)
        concierge = FlybitsConcierge.with(context)
        if (concierge?.isAuthenticated == true) {
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        concierge?.registerAuthenticationStateListener(this)

        button_login.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            is2Phase = false
            concierge?.authenticate(JwtIDP("eyJhbGciOiJIUzI1NiIsImtpZCI6IjAyN0VGQkU3LTAxMjMtNDgyMC04OTQ3LUI4NjM3Q0FCQTgzNSIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTYwMDA0OTUsIm5iZiI6MTU5MDgxNjQ5NSwidXNlcklEIjoiODBCQ0M3OUQtRjNGRi00MDlFLTlEMEQtRTkwN0Y1MEI1QTM4IiwiZGV2aWNlSUQiOiJDRjE4MjNGNC05REQxLTQzNkEtQkUxNy00MjdGQjY4NDlEQkQiLCJ0ZW5hbnRJRCI6IjAyN0VGQkU3LTAxMjMtNDgyMC04OTQ3LUI4NjM3Q0FCQTgzNSIsImlzU0EiOmZhbHNlfQ.tatXFCA6Iy4uXvkr7pqIBF_lIwGOyRhYWHzRjMzOXO4"))
        }
        button_login_anonymous.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            is2Phase = false
            concierge?.authenticate(AnonymousIDP())
        }


        button_two_phase.setOnClickListener {
            username = field_email.text.toString()
            password = field_password.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                progressBar?.visibility = View.GONE
                is2Phase = true
                findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
            } else {
                Toast.makeText(
                    context,
                    "Please enter username and password to connect",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        button_two_phase2.setOnClickListener {
            username = ""
            password = ""
            progressBar?.visibility = View.GONE
            is2Phase = true
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
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