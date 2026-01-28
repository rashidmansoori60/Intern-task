package com.example.interntask.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interntask.databinding.ItemBestdealsBinding
import com.example.interntask.model.MainhomeModel.Product
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class BestdealAdapter @Inject constructor(var products: List<Product>,val onclick: (Int)-> Unit):RecyclerView.Adapter<BestdealAdapter.BestdealVh>() {

    fun updatedata(list: List<Product>){

        this.products=list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestdealVh {

        val binding= ItemBestdealsBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return BestdealVh(binding)
    }

    override fun onBindViewHolder(
        holder: BestdealVh,
        position: Int
    ) {
        val product=products[position]
         holder.binding.productName.text=product.title
         holder.binding.productActualPrice.text=product.price.toIntValue().toINR()
         holder.binding.productDiscount.text="${product.discountPercentage.toIntValue()}% OFF"
         holder.binding.productPriceAfterDiscount.text=product.afterDiscountPrice.toIntValue().toINR()
         Glide.with(holder.itemView).load(product.thumbnail).into(holder.binding.productImg)
         holder.binding.productDescription.text=product.description

        holder.itemView.setOnClickListener {
            onclick(product.id)
        }

        if (product.discountPercentage > 0) {
            holder.binding.productActualPrice.paintFlags =
                holder.binding.productActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.productActualPrice.paintFlags =
                holder.binding.productActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }


    }

    override fun getItemCount(): Int {
     return products.size
    }

    class BestdealVh(val binding: ItemBestdealsBinding) : RecyclerView.ViewHolder(binding.root)

}


fun Int.toINR(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return format.format(this)
}

fun Double.toIntValue(): Int {
    return this.toInt()
}


fun Int.toINRint(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return format.format(this)
}