package com.example.interntask.adapters.Details
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interntask.adapters.toINR
import com.example.interntask.adapters.toIntValue
import com.example.interntask.databinding.GridItemBinding
import com.example.interntask.model.Detailsrvmodel.Horizontal_Gridmodel
import com.example.interntask.model.MainhomeModel.Product

class HorizontalitemAdapter(var list: List<Product>): RecyclerView.Adapter<HorizontalitemAdapter.Horizontalvh>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Horizontalvh {
        val binding= GridItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Horizontalvh(binding)
    }

    override fun onBindViewHolder(
        holder: Horizontalvh,
        position: Int
    ) {
        val result=list[position]
        holder.binding.productName.text=result.title
        Glide.with(holder.itemView).load(result.images[0]).into(holder.binding.productImg)
        holder.binding.productActualPrice.text=result.price.toIntValue().toINR()
        holder.binding.productPriceAfterDiscount.text=result.afterDiscountPrice.toIntValue().toINR()

    }



    override fun getItemCount(): Int {
       return list.size
    }

    class Horizontalvh(val binding: GridItemBinding): RecyclerView.ViewHolder(binding.root)







}