package com.example.interntask.Fragments.shoping_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.interntask.Fragments.cetegories_fragment.BeautyFragment
import com.example.interntask.Fragments.cetegories_fragment.Fashion_Fragment
import com.example.interntask.Fragments.cetegories_fragment.Furniture_Fragment
import com.example.interntask.Fragments.cetegories_fragment.MainhomeFragment
import com.example.interntask.Fragments.cetegories_fragment.accessories_Fragment
import com.example.interntask.Fragments.cetegories_fragment.mobile_Fragment
import com.example.interntask.R
import com.example.interntask.adapters.HomeviewpagerAdapter
import com.example.interntask.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class  HomeFragment : Fragment() {

    var _binding: FragmentHomeBinding?=null

    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout

    lateinit var viewpageradapter: FragmentStateAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout=binding.tabLayout

        val list=arrayListOf<Fragment>(MainhomeFragment(), Fashion_Fragment(), mobile_Fragment(),
            BeautyFragment(), Furniture_Fragment(), accessories_Fragment())


        viewpageradapter= HomeviewpagerAdapter(list,childFragmentManager,lifecycle)
        binding.homeViewpager.adapter=viewpageradapter
        binding.homeViewpager.isUserInputEnabled = false


        createTab(tabLayout,"Home",R.drawable.homee)
        createTab(tabLayout,"Fashion",R.drawable.shopping)
        createTab(tabLayout,"Mobiles",R.drawable.mobile)
        createTab(tabLayout,"Beauty",R.drawable.beuty)
        createTab(tabLayout,"Furniture",R.drawable.furniture)
        createTab(tabLayout,"Tablets",R.drawable.outline_aod_tablet_24)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.homeViewpager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })



    }

    private fun createTab(tabLayout: TabLayout, title: String, iconRes: Int) {
        val tab = tabLayout.newTab()
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.custum_tab, null)

        val tabIcon = view.findViewById<ImageView>(R.id.tabIcon)
        val tabText = view.findViewById<TextView>(R.id.tabText)

        tabIcon.setImageResource(iconRes)
        tabText.text = title

        tab.customView = view
        tabLayout.addTab(tab)
    }
}

