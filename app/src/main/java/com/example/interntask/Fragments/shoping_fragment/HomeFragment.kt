package com.example.interntask.Fragments.shoping_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.interntask.Fragments.cetegories_fragment.AccessoriesFragment
import com.example.interntask.Fragments.cetegories_fragment.FashionFragment
import com.example.interntask.Fragments.cetegories_fragment.FurnitureFragment
import com.example.interntask.Fragments.cetegories_fragment.MainhomeFragment
import com.example.interntask.Fragments.cetegories_fragment.Mobile_TabletsFragment
import com.example.interntask.R
import com.example.interntask.adapters.HomeviewpagerAdapter
import com.example.interntask.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class  HomeFragment : Fragment() {

    var _binding: FragmentHomeBinding?=null

    private val binding get() = _binding!!

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

        val list=arrayListOf<Fragment>(MainhomeFragment(), AccessoriesFragment(), FurnitureFragment(),
            Mobile_TabletsFragment(), FashionFragment())

        viewpageradapter= HomeviewpagerAdapter(list,childFragmentManager,lifecycle)
        binding.homeViewpager.adapter=viewpageradapter

        TabLayoutMediator(binding.tabLayout,binding.homeViewpager){tab,position ->
            when(position){

                 0 ->{
                    tab.text="Main"
                }
                1->{
                    tab.text="Accessories"
                }
                2->{
                    tab.text="Furniture"
                }
                3->{
                    tab.text="Mobile & Tablets"
                }
                4->{
                    tab.text="Fashion"
                }

            }

        }.attach()

    }

}