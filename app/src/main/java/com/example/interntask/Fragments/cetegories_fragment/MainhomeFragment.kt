package com.example.interntask.Fragments.cetegories_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.interntask.R
import com.example.interntask.databinding.FragmentMainhomeBinding

class MainhomeFragment : Fragment() {

   var _binding: FragmentMainhomeBinding?=null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMainhomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root


    }


}