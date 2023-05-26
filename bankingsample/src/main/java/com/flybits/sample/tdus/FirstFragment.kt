package com.flybits.sample.tdus

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flybits.concierge.Concierge
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.ConciergeParams
import com.flybits.concierge.enums.Container
import com.flybits.sample.tdus.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var callback : Interaction

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadConcierge()
        return root

    }

    fun loadConcierge(){
        val zonesConfig = Concierge.zonesConfiguration(requireContext(), listOf("homepage"))
        Concierge.fragment(
            context = requireContext(),
            type = Container.Configured,
            conciergeParams = arrayListOf(ConciergeParams.ZonesFilter(zonesConfig)),
            conciergeOptions = arrayListOf(ConciergeOptions.CustomLoadingView(
                LoadingViewProviderClient()
            ))
        ).let {
            callback.updateConcierge(R.id.layoutConcierge, it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            callback = context as Interaction
        }catch (e : Exception ){}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}