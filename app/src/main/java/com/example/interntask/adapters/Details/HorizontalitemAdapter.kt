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

class HorizontalitemAdapter(var list: MutableList<Product>,val onclick:(Int)-> Unit): RecyclerView.Adapter<HorizontalitemAdapter.Horizontalvh>() {


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


        holder.itemView.setOnClickListener {
            onclick(result.id)
        }
    }



    override fun getItemCount(): Int {
       return list.size
    }

    class Horizontalvh(val binding: GridItemBinding): RecyclerView.ViewHolder(binding.root)







}