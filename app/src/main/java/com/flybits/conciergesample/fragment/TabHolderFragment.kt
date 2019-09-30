package com.flybits.conciergesample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.flybits.concierge.ConciergeFragment
import com.flybits.concierge.DisplayConfiguration
import com.flybits.concierge.enums.ShowMode
import com.flybits.conciergesample.R
import kotlinx.android.synthetic.main.fragment_tab_holder.*

class TabHolderFragment: Fragment() {

    class TabViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AccountFragment.newInstance()
                1 -> ConciergeFragment.newInstance(
                    DisplayConfiguration(
                        ConciergeFragment.MenuType.MENU_TYPE_APP_BAR,
                        ShowMode.OVERLAY,
                        true
                    )

                )
                else -> throw IllegalStateException("Tab position $position does not exist")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Account"
                1 -> "Concierge"
                else -> throw IllegalStateException("Tab position $position does not exist")
            }
        }

        override fun getCount() = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_holder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> (activity as AppCompatActivity).supportActionBar?.title = "Account"
                    1 -> (activity as AppCompatActivity).supportActionBar?.title = "Concierge"
                    else -> throw IllegalStateException("Tab position $position does not exist")
                }
            }
        })
        view_pager.adapter = TabViewPagerAdapter(activity!!.supportFragmentManager)
        tabs.setupWithViewPager(view_pager)
    }
}