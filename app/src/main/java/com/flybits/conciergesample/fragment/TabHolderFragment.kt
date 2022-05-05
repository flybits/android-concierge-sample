package com.flybits.conciergesample.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.flybits.android.push.models.newPush.Push
import com.flybits.concierge.ConciergeConstants
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.Container
import com.flybits.conciergesample.R
import kotlinx.android.synthetic.main.fragment_tab_holder.*

class TabHolderFragment : Fragment() {

    private var isHorizontal: Boolean = false
    private var displayNotificationAPI: Boolean = false
    var containerType: Container = Container.None
    private var settings: Boolean = false
    var notification: Boolean = false
    private var displaynavigation: Boolean = false
    var pushExtra: Push? = null

    class TabViewPagerAdapter(
        fragmentManager: FragmentManager,
        var context: Context,
        var isHorizontal: Boolean,
        var displayNotificationAPI: Boolean,
        var containerType: Container,
        var settings: Boolean,
        var notification: Boolean,
        var displaynavigation: Boolean,
        var pushExtra: Push? = null
    ) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            val conciergeOptions = ArrayList<ConciergeOptions>()
            if (isHorizontal) {
                conciergeOptions.add(ConciergeOptions.Horizontal)
            }
            if (displayNotificationAPI) {
                conciergeOptions.add(ConciergeOptions.ShowNotifications)
            }
            if (displaynavigation) {
                conciergeOptions.add(ConciergeOptions.DisplayNavigation)
            }
            if (settings) {
                conciergeOptions.add(ConciergeOptions.Settings)
            }
            if (notification) {
                conciergeOptions.add(ConciergeOptions.Notifications)
            }

            return when (position) {
                0 -> {
                    AccountFragment.newInstance()
                }
                else -> throw IllegalStateException("Tab position $position does not exist")
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Account"
                /*1 -> "Concierge"*/
                else -> throw IllegalStateException("Tab position $position does not exist")
            }
        }

        override fun getCount() = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_holder, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    //findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            isHorizontal = this.getBoolean("showHorizontal")
            displayNotificationAPI = this.getBoolean("showNotificationsApi")
            containerType = this.getParcelable("ContainerType") ?: Container.Categories
            settings = this.getBoolean("settings")
            notification = this.getBoolean("notification")
            displaynavigation = this.getBoolean("displaynavigation")
            if (this.getParcelable<Push>(ConciergeConstants.PUSH_EXTRA) != null) {
                pushExtra = this.getParcelable<Push>(ConciergeConstants.PUSH_EXTRA)
            }
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> (activity as AppCompatActivity).supportActionBar?.title = "Account"
                    1 -> (activity as AppCompatActivity).supportActionBar?.title = "Concierge"
                    else -> throw IllegalStateException("Tab position $position does not exist")
                }
            }
        })
        view_pager.adapter =
            TabViewPagerAdapter(
                requireActivity().supportFragmentManager,
                requireContext(),
                isHorizontal,
                displayNotificationAPI,
                containerType,
                settings,
                notification,
                displaynavigation,
                pushExtra
            )
        tabs.setupWithViewPager(view_pager)
    }
}