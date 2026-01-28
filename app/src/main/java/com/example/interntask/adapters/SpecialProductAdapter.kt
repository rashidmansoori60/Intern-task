package com.example.interntask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interntask.databinding.GridItemBinding
import com.example.interntask.model.MainhomeModel.Product

class SpecialProductAdapter(var products: List<Product>): RecyclerView.Adapter<SpecialProductAdapter.SpecialVh>() {

    fun updatedata(list: List<Product>){
        this.products=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecialVh {
        val bindind= GridItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SpecialVh(bindind)
    }

    override fun onBindViewHolder(
        holder: SpecialVh,
        position: Int
    ) {
        val product=products[position]
        holder.binding.productActualPrice.text=product.price.toIntValue().toINR()
        holder.binding.productPriceAfterDiscount.text=product.afterDiscountPrice.toIntValue().toINR()
        holder.binding.productName.text=product.title
        Glide.with(holder.itemView).load(product.thumbnail).into(holder.binding.productImg)

    }

    override fun getItemCount(): Int {
        return products.size
    }

    class SpecialVh(val binding: GridItemBinding) : RecyclerView.ViewHolder(binding.root)
}