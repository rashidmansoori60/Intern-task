package com.example.interntask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interntask.databinding.ItemDetailsimagesBinding
import javax.inject.Inject

class DetailsVpAdapter @Inject constructor(
    private var images: List<String>
) : RecyclerView.Adapter<DetailsVpAdapter.ImageVH>() {

    fun submitList(newImages: List<String>) {
        this.images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageVH {
        val binding = ItemDetailsimagesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageVH(binding)
    }

    override fun onBindViewHolder(
        holder: ImageVH,
        position: Int
    ) {
        val imageUrl = images[position]

        Glide.with(holder.binding.detailsimage.context)
            .load(imageUrl)
            .into(holder.binding.detailsimage)
    }

    override fun getItemCount(): Int = images.size

    class ImageVH(val binding: ItemDetailsimagesBinding) :
        RecyclerView.ViewHolder(binding.root)
}
