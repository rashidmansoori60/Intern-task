package com.example.interntask.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interntask.databinding.AllmixItemBinding
import com.example.interntask.databinding.ItemBannerBinding
import com.example.interntask.model.Bannermodel.Photo
import com.example.interntask.model.MainhomeModel.Product
import javax.inject.Inject

class MixallAdapter @Inject constructor(val product: MutableList<Product>,val onclick:(Int)-> Unit): RecyclerView.Adapter<MixallAdapter.AllitemVh>() {

    fun additem(data: List<Product>){
        val size=product.size
        product.addAll(data)
        notifyItemRangeInserted(size,data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllitemVh {
        val binding= AllmixItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AllitemVh(binding)
    }

    override fun onBindViewHolder(holder: AllitemVh, position: Int) {
        val item=product[position]
        holder.binding.txtTitle.text=item.title
        holder.binding.txtOriginalPrice.text=item.price.toIntValue().toINR()
        holder.binding.txtDiscountPrice.text=item.afterDiscountPrice.toIntValue().toINR()
        holder.binding.txtDiscountPercent.text="${item.discountPercentage.toIntValue()}% OFF"
        Glide.with(holder.itemView).load(item.thumbnail).into(holder.binding.imgProduct)
        holder.binding.txtOriginalPrice.paintFlags =
            holder.binding.txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.itemView.setOnClickListener {
            onclick(item.id)
        }

    }

    override fun getItemCount(): Int {
        return product.size
    }


    class AllitemVh(val binding: AllmixItemBinding): RecyclerView.ViewHolder(binding.root)
}