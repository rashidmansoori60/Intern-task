package com.example.interntask.adapters.Details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interntask.adapters.toINR
import com.example.interntask.adapters.toIntValue
import com.example.interntask.databinding.GridItemBinding
import com.example.interntask.databinding.ProductItemBinding
import com.example.interntask.model.MainhomeModel.Product

class GridAdapter(var list: MutableList<Product>): RecyclerView.Adapter<GridAdapter.Gridvh>() {

    fun setInitialData(newList: List<Product>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun addMoreItems(newList: List<Product>) {
        val start = list.size
        list.addAll(newList)
        notifyItemRangeInserted(start, newList.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Gridvh {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Gridvh(binding)
    }

    override fun onBindViewHolder(
        holder: Gridvh,
        position: Int
    ) {
        val result = list[position]
        holder.binding.productName.text = result.title
        Glide.with(holder.itemView).load(result.images[0]).into(holder.binding.productImg)
        holder.binding.productPrice.text = result.price.toIntValue().toINR()
        holder.binding.productPriceafterdiscount.text =
            result.afterDiscountPrice.toIntValue().toINR()
        holder.binding.productDiscount.text = "${result.discountPercentage.toIntValue()}% OFF"

    }


    override fun getItemCount(): Int {
        return list.size
    }

    class Gridvh(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)

}





