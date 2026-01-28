package com.example.interntask.Fragments.cetegories_fragment

import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.interntask.R
import com.example.interntask.Uistate.Uistate
import com.example.interntask.adapters.BannerAdapter
import com.example.interntask.adapters.BestdealAdapter
import com.example.interntask.adapters.MixallAdapter
import com.example.interntask.adapters.SpecialProductAdapter
import com.example.interntask.databinding.FragmentMainhomeBinding
import com.example.interntask.model.Bannermodel.Photo
import com.example.interntask.model.MainhomeModel.Product
import com.example.interntask.viewmodels.BannerVm
import com.example.interntask.viewmodels.BestdealsVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainhomeFragment : Fragment() {
    private val TAG = "MainHomeLife"
    private var autoSlideJob: Job? = null
    private val SLIDE_DELAY = 3000L   // 3 sec

    val pageSize = 20
    var currentPage = 0
    val displayedItems = mutableListOf<Product>()

    val allmixAdapter = MixallAdapter(displayedItems)


    private var isIndicatorAttached = false

    lateinit var adapter: BannerAdapter

    val allItems = mutableListOf<Product>()

    lateinit var specialProductAdapter: SpecialProductAdapter

    lateinit var beastdealAdapter: BestdealAdapter
     val bannerVm: BannerVm by activityViewModels()

    val bestdealsVm: BestdealsVm by activityViewModels()
    var _binding: FragmentMainhomeBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        _binding= FragmentMainhomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onviewcreate")
        beastdealAdapter= BestdealAdapter(emptyList()){id ->

            bestdealsVm.getbyId(id)
            requireActivity()
                .findNavController(R.id.nav_host)
                .navigate(R.id.productDetailsFragment)


        }

        adapter= BannerAdapter(mutableListOf())
        binding.bannerimgVp.adapter = adapter
        binding.bannerimgVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.bannerimgVp.isUserInputEnabled = true
        binding.bestdealRec.adapter=beastdealAdapter
        binding.bestdealRec.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)


        specialProductAdapter= SpecialProductAdapter(emptyList())

        binding.specialProduct.adapter=specialProductAdapter
        binding.specialProduct.layoutManager= GridLayoutManager(requireContext(),3)

    //#####################################################################################
        
        binding.allmixgridRecyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        binding.allmixgridRecyclerView.adapter=allmixAdapter




        bannerVm.getPhoto()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    bannerVm.bannerPhoto.collect { it ->

                        when (it) {
                            is Uistate.Error -> {
                                binding.bannerProgress.visibility = View.GONE


                            }

                            is Uistate.Loading -> {
                                binding.bannerProgress.visibility = View.VISIBLE
                            }

                            is Uistate.Success -> {
                                binding.bannerProgress.visibility = View.GONE
                                adapter.additem(it.data)
                                if (adapter.itemCount > 1 && !isIndicatorAttached) {
                                    binding.dotsIndicator.attachTo(binding.bannerimgVp)
                                    isIndicatorAttached = true
                                    startAutoSlide()
                                }
                            }
                        }
                    }

                }

                launch {
                    bannerVm.toast.collect { value ->
                        Toast.makeText(requireContext(), value, Toast.LENGTH_LONG).show()
                    }
                }


                    launch {
                        bestdealsVm.toastbestdeal.collect { value ->
                            Log.e("errorr",value)
                            Toast.makeText(requireContext(), value, Toast.LENGTH_LONG).show()
                        }

                }

                launch {
                    bestdealsVm.bestDeals.collect { it ->
                        when(it){
                            is Uistate.Error -> {
                                binding.bestDealProgress.visibility= View.GONE
                            }

                            is Uistate.Success -> {
                                binding.bestDealProgress.visibility= View.GONE
                                beastdealAdapter.updatedata(it.data)

                            }

                            is Uistate.Loading -> {
                                binding.bestDealProgress.visibility= View.VISIBLE

                            }
                        }
                    }
                }
                launch {
                    bestdealsVm.specialProduct.collect { it->
                        when(it){

                            is Uistate.Success -> {
                                specialProductAdapter.updatedata(it.data)
                                binding.specialProductProgress.visibility= View.GONE
                            }

                            is Uistate.Loading -> {
                                binding.specialProductProgress.visibility= View.VISIBLE

                            }

                            is Uistate.Error -> {
                                binding.specialProductProgress.visibility= View.GONE

                            }
                        }

                    }
                }

                launch {
                    bestdealsVm.batchFlow.collect {it->
                        allmixAdapter.additem(it)
                    }
                }
            }
        }


        binding.allmixgridRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {

                if (dy <= 0) return
                val lm = rv.layoutManager as GridLayoutManager

                val lastVisible = lm.findLastVisibleItemPosition()
                val total = allmixAdapter.itemCount

                if (lastVisible >= total - 1) {
                    bestdealsVm.emitnextItem()
                }
            }})


    }
    private fun startAutoSlide() {
        autoSlideJob?.cancel()

        autoSlideJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(SLIDE_DELAY)

                val count = adapter.itemCount
                if (count <= 1) continue

                val next = (binding.bannerimgVp.currentItem + 1) % count
                binding.bannerimgVp.setCurrentItem(next, true)
            }
        }
    }



    override fun onResume() {
        Log.d(TAG, "resume")
        super.onResume()
        if(adapter.itemCount>1){
        startAutoSlide()
        }
    }
    override fun onPause() {
        Log.d(TAG, "pause")
        super.onPause()
        autoSlideJob?.cancel()
        autoSlideJob = null
    }

    override fun onDestroyView() {
        Log.d(TAG, "destroyview")
        super.onDestroyView()
        isIndicatorAttached = false
        autoSlideJob?.cancel()
        autoSlideJob = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "destroy")
    }
}