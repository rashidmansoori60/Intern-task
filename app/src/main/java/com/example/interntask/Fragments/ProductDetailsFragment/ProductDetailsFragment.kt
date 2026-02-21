package com.example.interntask.Fragments.ProductDetailsFragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interntask.R
import com.example.interntask.Uistate.Uistate
import com.example.interntask.adapters.Details.DetailSuggetionAdapter
import com.example.interntask.adapters.DetailsVpAdapter
import com.example.interntask.databinding.FragmentMainhomeBinding
import com.example.interntask.databinding.FragmentProductDetailsBinding
import com.example.interntask.model.Detailsrvmodel.DetailsAll_itemmodel
import com.example.interntask.model.MainhomeModel.Product
import com.example.interntask.viewmodels.BestdealsVm
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.getValue
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    var _binding: FragmentProductDetailsBinding?=null
    private val binding get() = _binding!!

    lateinit var list: MutableList<DetailsAll_itemmodel>
    private val mutableList=mutableListOf<DetailsAll_itemmodel>()



    val bestdealsVm: BestdealsVm by activityViewModels()

    // val secrtion=mutableListOf<DetailsAll_itemmodel>()

   lateinit var detailSuggetionAdapter: DetailSuggetionAdapter

    lateinit var vpAdapter: DetailsVpAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentProductDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("productdetails", "onviewcreted")


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {

                // agar stack me previous item hai
                if (bestdealsVm.itemstack.size > 1) {
                    bestdealsVm.popitemstack()
                    //    findNavController().popBackStack()
                } else if (bestdealsVm.itemstack.size == 1) {
                    bestdealsVm.popitemstack()
                    findNavController().popBackStack()
                }
            }

        vpAdapter = DetailsVpAdapter(emptyList())

        binding.viewPagerProductImages.adapter = vpAdapter



        detailSuggetionAdapter = DetailSuggetionAdapter(
            mutableListOf(),
            loadmore = {
                viewLifecycleOwner.lifecycleScope.launch {
                    bestdealsVm.detailAllitememitshared()
                }
            },
            onclick = { it ->
                bestdealsVm.getbyId(it)
                requireActivity()
                    .findNavController(R.id.nav_host)
                    .navigate(R.id.productDetailsFragment)
            })





        binding.MainRvSuggestions.layoutManager = LinearLayoutManager(requireContext())

        binding.MainRvSuggestions.adapter = detailSuggetionAdapter


        viewLifecycleOwner.lifecycleScope.launch {
            bestdealsVm.detailAllGridshared.collect { it ->
                detailSuggetionAdapter.addmoreGriditem(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    bestdealsVm.detailItem.collect { it ->
                        when (it) {
                            is Uistate.Error -> {
                                binding.detailsprogress.visibility = View.GONE

                            }

                            is Uistate.Loading -> {
                                binding.detailsprogress.visibility = View.VISIBLE
                                binding.mainContent.visibility = View.GONE
                            }

                            is Uistate.Success -> {
                                binding.detailsprogress.visibility = View.GONE
                                binding.mainContent.visibility = View.VISIBLE
                                animateProductChange { bindProduct(it.data) }
                            }
                        }
                    }

                }
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                combine(
                    bestdealsVm.detailsuggetionA,
                    bestdealsVm.detailsuggetionB,
                    bestdealsVm.allGriditem
                ) { a, b, c ->
                    Triple(a, b, c)
                }
                    .catch { e ->
                        Log.e("combineError", e.message.toString())
                    }.collect { (a, b, c) ->
                        val finalList = mutableListOf<DetailsAll_itemmodel>()

                        if (a is Uistate.Success) {
                            finalList.add(DetailsAll_itemmodel.SuggetionA(a.data))
                        }

                        if (b is Uistate.Success) {
                            finalList.add(DetailsAll_itemmodel.SuggetionB(b.data))
                        }

                        if (c is Uistate.Success) {
                            finalList.add(DetailsAll_itemmodel.Allproduct(c.data))
                        }

                        detailSuggetionAdapter.update(finalList)

                    }

            }
        }
    }
//                launch {
//                    bestdealsVm.detailsuggetionA.collect { it ->
//                        when (it) {
//                            is Uistate.Loading -> {}
//
//                            is Uistate.Success -> {
//                                Toast.makeText(requireContext(),it.data.size.toString()+"A", Toast.LENGTH_LONG).show()
//                                mutableList.removeAll{it is DetailsAll_itemmodel.SuggetionA}
//                                mutableList.add(DetailsAll_itemmodel.SuggetionA(it.data))
//                                refreshAdapterDebounced()
//                               // detailSuggetionAdapter.update(DetailsAll_itemmodel.SuggetionA(it.data))
//
////                                if (mutableList.none { it is DetailsAll_itemmodel.SuggetionA }) {
//////                                    mutableList.add(DetailsAll_itemmodel.SuggetionA(it.data))
//////                                    detailSuggetionAdapter.update(mutableList.toList())
////                                }
//                            }
//
//                            is Uistate.Error -> {}
//
//
//                        }
//                    }
//                }
//                launch {
//                    bestdealsVm.detailsuggetionB.collect { it ->
//                        when (it) {
//                            is Uistate.Loading -> {
//
//                            }
//
//                            is Uistate.Success -> {
//                                Toast.makeText(requireContext(),it.data.size.toString()+"B", Toast.LENGTH_LONG).show()
//                                mutableList.removeAll{it is DetailsAll_itemmodel.SuggetionB}
//                                mutableList.add(DetailsAll_itemmodel.SuggetionB (it.data))
//                                refreshAdapterDebounced()
//                               // detailSuggetionAdapter.update(DetailsAll_itemmodel.SuggetionB(it.data))
////                                if (mutableList.none { it is DetailsAll_itemmodel.SuggetionB }) {
////
////
//////                                    mutableList.add(DetailsAll_itemmodel.SuggetionB(it.data))
//////                                    detailSuggetionAdapter.update(mutableList.toList())
////                                }
//                            }
//
//                            is Uistate.Error -> {
//
//                            }
//
//                        }
//                    }
//
//                }
//                launch {
//                    bestdealsVm.allGriditem.collect { it ->
//                        when (it) {
//                            is Uistate.Loading -> {
//
//                            }
//
//                            is Uistate.Success -> {
//                                Toast.makeText(requireContext(),it.data.size.toString()+"Allll", Toast.LENGTH_LONG).show()
//
//                                mutableList.removeAll{it is DetailsAll_itemmodel.Allproduct}
//                                mutableList.add(DetailsAll_itemmodel.Allproduct(it.data))
//                                refreshAdapterDebounced()
////                                if (mutableList.none { it is DetailsAll_itemmodel.Allproduct }) {
////                                    detailSuggetionAdapter.update(DetailsAll_itemmodel.Allproduct(it.data))
//////                                    mutableList.add(DetailsAll_itemmodel.Allproduct(it.data))
//////                                    detailSuggetionAdapter.update(mutableList.toList())
////                                }
//
//                            }
//
//                            is Uistate.Error -> {
//
//                            }
//
//                        }
//                    }
//
//                }
//
//            }
//
//
//
//        }}

        @RequiresApi(Build.VERSION_CODES.M)
        private fun bindProduct(product: Product) = with(binding) {

            binding.scrollView.scrollTo(0, 0)

            // ---------- BASIC INFO ----------
            tvTitle.text = product.title
            tvDescription.text = product.description

            // ---------- RATING ----------
            tvRating.text = String.format("%.1f", product.rating)
            tvReviewCount.text = "(${product.reviews.size})"

            // ---------- PRICE CALCULATION ----------
            val discountedPrice =
                product.price - (product.price * product.discountPercentage / 100)

            tvDiscountPercent.text = "${product.discountPercentage.toInt()}%"
            tvOriginalPrice.text = "₹${product.price}"
            tvDiscountedPrice.text = "₹${String.format("%.2f", discountedPrice)}"

            // ---------- STOCK / AVAILABILITY ----------
            if (product.availabilityStatus.equals("In Stock", true) && product.stock > 0) {
                tvStock.text = "In stock"
                tvStock.setTextColor(requireContext().getColor(R.color.g_green))
            } else {
                tvStock.text = "Out of stock"
                tvStock.setTextColor(Color.RED)
            }

            // ---------- PRODUCT DETAILS ----------
            tvBrand.text = product.brand ?: "N/A"
            tvSku.text = product.sku
            tvWeight.text = "${product.weight} g"

            tvDimensions.text =
                "${product.dimensions.width} × ${product.dimensions.height} × ${product.dimensions.depth} cm"

            tvWarranty.text = product.warrantyInformation
            tvShipping.text = product.shippingInformation

            // ---------- VIEWPAGER IMAGES ----------

            vpAdapter.submitList(product.images)

        }


        override fun onResume() {
            super.onResume()
            Log.e("productdetails", "onresume")
            requireActivity()
                .findViewById<BottomNavigationView>(R.id.bottom_navigation)
                .isVisible = false
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
            Log.e("productdetails", "onDestroyview")
            requireActivity()
                .findViewById<BottomNavigationView>(R.id.bottom_navigation)
                .isVisible = true
        }


        override fun onDestroy() {
            super.onDestroy()
            Log.e("productdetails", "onDestroy")
        }

        override fun onDetach() {
            super.onDetach()
            Log.e("productdetails", "onDestroy")
        }

        private fun refreshAdapter() {
            val finalList = mutableListOf<DetailsAll_itemmodel>()

            mutableList.find { it is DetailsAll_itemmodel.SuggetionA }?.let {
                finalList.add(it)
            }

            mutableList.find { it is DetailsAll_itemmodel.SuggetionB }?.let {
                finalList.add(it)

            }

            mutableList.find { it is DetailsAll_itemmodel.Allproduct }?.let {
                finalList.add(it)

            }
            detailSuggetionAdapter.update(finalList)
        }


        private var isAnimating = false

        private fun animateProductChange(bind: () -> Unit) {

            if (isAnimating) return
            isAnimating = true

            binding.contentContainer.animate().cancel()
            binding.viewPagerProductImages.animate().cancel()

            binding.contentContainer.animate()
                .alpha(0f)
                .translationY(20f)
                .setDuration(120)
                .withEndAction {

                    bind()

                    binding.contentContainer.translationY = -20f
                    binding.contentContainer.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(200)
                        .withEndAction {
                            isAnimating = false
                        }
                        .start()
                }
                .start()

            binding.viewPagerProductImages.animate()
                .scaleX(0.96f)
                .scaleY(0.96f)
                .alpha(0.7f)
                .setDuration(120)
                .withEndAction {
                    binding.viewPagerProductImages.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
                .start()
        }

        private var suggestionJob: Job? = null

        private fun refreshAdapterDebounced() {
            suggestionJob?.cancel()
            suggestionJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(80) // 🔥 wait for all flows
                refreshAdapter()
            }
        }
    }

