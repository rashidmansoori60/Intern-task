package com.example.interntask.Fragments.cetegories_fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.interntask.R
import com.example.interntask.databinding.FragmentMainhomeBinding
import com.example.interntask.databinding.FragmentSearchtypeBinding
import com.example.interntask.viewmodels.BestdealsVm
import kotlin.getValue

class SearchtypeFragment : Fragment() {


    var _binding: FragmentSearchtypeBinding?=null
    private val binding get() = _binding!!

    val bestdealsVm: BestdealsVm by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSearchtypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

           bestdealsVm.onsearchTextchange(s.toString())


            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

}