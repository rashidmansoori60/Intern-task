package com.example.interntask.Fragments.ProductDetailsFragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.launch
import kotlin.getValue
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    var _binding: FragmentProductDetailsBinding?=null
    private val binding get() = _binding!!

    lateinit var list: MutableList<DetailsAll_itemmodel>
    var mutableList=mutableListOf<DetailsAll_itemmodel>()
    val bestdealsVm: BestdealsVm by activityViewModels()

   lateinit var detailSuggetionAdapter: DetailSuggetionAdapter

    lateinit var vpAdapter: DetailsVpAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentProductDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         vpAdapter= DetailsVpAdapter(emptyList())

         binding.viewPagerProductImages.adapter=vpAdapter



        detailSuggetionAdapter= DetailSuggetionAdapter(emptyList())
        binding.MainRvSuggestions.adapter=detailSuggetionAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

              launch {
            bestdealsVm.detailsuggetionA.collect { it ->
                when(it) {
                    is Uistate.Loading -> {}

                    is Uistate.Success -> {
                        mutableList.add(DetailsAll_itemmodel.SuggetionA(it.data))
                    }

                    is Uistate.Error -> {}


                }
            }}
                  launch {
                      bestdealsVm.detailsuggetionB.collect { it ->
                          when(it){
                          is Uistate.Loading -> {

                      }

                          is Uistate.Success -> {
                              mutableList.add(DetailsAll_itemmodel.SuggetionB(it.data))
                          }

                          is Uistate.Error -> {

                          }

        }  }
                  }

                launch {
                    bestdealsVm.detailAllGrid.collect { it ->
                     mutableList.add(DetailsAll_itemmodel.Allproduct(it))
                    }
                }
                launch {
                    bestdealsVm.allGriditem.collect { it->
                        when(it){
                            is Uistate.Loading -> {

                            }

                            is Uistate.Success -> {
                                mutableList.add(DetailsAll_itemmodel.Allproduct(it.data))
                            }

                            is Uistate.Error -> {

                            }

                        }
                    }
                }

            }

        viewLifecycleOwner.lifecycleScope.launch {
            bestdealsVm.detailItem.collect { it ->
                when(it){
                    is Uistate.Error ->{
                        binding.detailsprogress.visibility=View.GONE

                    }

                    is Uistate.Loading -> {
                        binding.detailsprogress.visibility=View.VISIBLE
                    }

                    is Uistate.Success ->{
                        bindProduct(it.data)
                        binding.detailsprogress.visibility=View.GONE
                    }
                }
            }

        }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun bindProduct(product: Product) = with(binding) {



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
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .isVisible = true
    }

}