package com.example.interntask.adapters.Searchfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interntask.databinding.ItemProductlistBinding
import com.example.interntask.helper.toIntValue
import com.example.interntask.model.MainhomeModel.Product

class ResultproductsAdapter(var product: MutableList<Product>,val onItemClick: (Int) -> Unit,val onAddToCartClick: (Product) -> Unit): RecyclerView.Adapter<ResultproductsAdapter.ProductViewHolder>() {


    fun submitlist(newlist: List<Product>, isold: Boolean){

        if(isold){
            var startposition=this.product.size
            this.product.addAll(newlist)
            notifyItemRangeInserted(startposition,newlist.size)
        }
        else{
            this.product.clear()
            this.product.addAll(newlist)
            notifyDataSetChanged()
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
       val binding= ItemProductlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.bind(product[position])
    }

    override fun getItemCount(): Int {
return product.size
    }

    inner class ProductViewHolder(val binding: ItemProductlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            binding.tvProductName.text = product.title
            binding.tvPrice.text = "₹${product.afterDiscountPrice.toIntValue()}"
            binding.tvCutPrice.text = "₹${product.price}"
            binding.tvDiscount.text = "${product.discountPercentage.toIntValue()}% off"
            binding.tvRating.text = String.format("%.1f", product.rating)
            binding.tvReviewCount.text = "(${product.reviews.size})"

            // Load Image
            Glide.with(binding.root.context)
                .load(product.images[0])
                .into(binding.ivProduct)

            // Item Click
            binding.root.setOnClickListener {
                onItemClick(product.id)
            }

            // Add To Cart Click
            binding.btnAddToCart.setOnClickListener {
                onAddToCartClick(product)
            }
        }
    }
}