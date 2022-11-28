package com.flybits.conciergesample.zones_and_modules

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.flybits.concierge.Concierge
import com.flybits.concierge.enums.ConciergeParams
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP

class FragmentForFullScreenZone : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fullscreen_zone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val transaction = childFragmentManager.beginTransaction()
        Concierge.fragment(
            requireContext(),
            Container.Configured,
            arrayListOf(ConciergeParams.NamedConfig("testing")),
            null
        ).let {
            transaction.replace(R.id.fragment_container, it)
                .addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login_flybits) {
            // Check if connected already.
            if (!Concierge.isConnected(requireContext())) {
                // Call Connect and load the fragment.
                Concierge.connect(requireContext(), AnonymousConciergeIDP())
            } else {
                Concierge.disconnect(requireContext(), null)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}