package com.example.interntask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.ViewInfo
import com.bumptech.glide.Glide
import com.example.interntask.databinding.ItemBannerBinding
import com.example.interntask.model.Bannermodel.Photo
import com.example.interntask.model.Bannermodel.PhotoResponse
import com.google.firebase.database.core.Context
import javax.inject.Inject

class BannerAdapter @Inject constructor(val photos: MutableList<Photo>): RecyclerView.Adapter<BannerAdapter.Bannervh>() {

    fun additem(data: List<Photo>){
            val size=photos.size
          photos.addAll(data)
            notifyItemRangeInserted(size,data.size)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bannervh {
        val binding= ItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Bannervh(binding)
    }

    override fun onBindViewHolder(holder: Bannervh, position: Int) {
        val photo=photos[position]
        Glide.with(holder.itemView).load(photo.src.landscape).into(holder.binding.bannerImage)

    }

    override fun getItemCount(): Int {
            return photos.size
    }


    class Bannervh(val binding: ItemBannerBinding): RecyclerView.ViewHolder(binding.root)
}