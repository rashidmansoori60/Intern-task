package com.example.interntask.Fragments.cetegories_fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interntask.R
import com.example.interntask.Uistate.Searchstate
import com.example.interntask.Uistate.Uistate
import com.example.interntask.adapters.Searchfragment.Searchadapter
import com.example.interntask.adapters.SpecialProductAdapter
import com.example.interntask.databinding.FragmentMainhomeBinding
import com.example.interntask.databinding.FragmentSearchtypeBinding
import com.example.interntask.viewmodels.BestdealsVm
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue
@AndroidEntryPoint
class SearchtypeFragment : Fragment() {

    var _binding: FragmentSearchtypeBinding?=null
    private val binding get() = _binding!!

    val bestdealsVm: BestdealsVm by activityViewModels()

    lateinit var adapter: Searchadapter


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
        adapter= Searchadapter(mutableListOf())
        binding.rvSuggestions.layoutManager= LinearLayoutManager(requireContext())
        binding.rvSuggestions.adapter=adapter


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


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bestdealsVm.searchflow.collect { it->
                    when(it){
                        is Searchstate.Error -> {}

                        is Searchstate.Livesearch -> {

                            if (it.list.isEmpty()) {
                                adapter.submitlist(emptyList(), false)
                                binding.rvSuggestions.visibility = View.GONE
                                binding.nodataText.visibility = View.VISIBLE

                            } else {

                                adapter.submitlist(it.list, false)
                                binding.rvSuggestions.visibility = View.VISIBLE
                                binding.nodataText.visibility = View.GONE
                            }
                        }


                        is Searchstate.Loading -> {
                              showLoading()
                        }
                        is Searchstate.Recentsearch -> {

                        }

                        is Searchstate.Resultsproduct -> {
                            showProducts()

                        }
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        Log.e("productdetails","onresume")
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.submitlist(emptyList(),false)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvSuggestions.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE
        binding.nodataText.visibility = View.GONE
    }

    private fun showSuggestions() {
        binding.progressBar.visibility = View.GONE
        binding.rvSuggestions.visibility = View.VISIBLE
        binding.rvProducts.visibility = View.GONE
        binding.nodataText.visibility = View.GONE
    }

    private fun showProducts() {
        binding.progressBar.visibility = View.GONE
        binding.rvSuggestions.visibility = View.GONE
        binding.rvProducts.visibility = View.VISIBLE
        binding.nodataText.visibility = View.GONE
    }

    private fun showNoData() {
        binding.progressBar.visibility = View.GONE
        binding.rvSuggestions.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE
        binding.nodataText.visibility = View.VISIBLE
    }

}