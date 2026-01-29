package com.example.interntask.adapters.Details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interntask.databinding.DetailItemSuggetiononeBinding
import com.example.interntask.databinding.DetailsAllGridBinding
import com.example.interntask.databinding.DetailsItemsuggetionTwoBinding
import com.example.interntask.model.Detailsrvmodel.DetailsAll_itemmodel
import com.example.interntask.model.Detailsrvmodel.Horizontal_Gridmodel
import com.example.interntask.model.MainhomeModel.Product

class DetailSuggetionAdapter(var list: List<DetailsAll_itemmodel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun update(list: List<DetailsAll_itemmodel>){
        this.list=list
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is DetailsAll_itemmodel.SuggetionA -> 0
            is DetailsAll_itemmodel.SuggetionB -> 1
            is DetailsAll_itemmodel.Allproduct -> 2
        }
    }




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
      return when(viewType){
             0->{
               val view= DetailItemSuggetiononeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                    SuggertionAvh(view)

            }
             1->{
              val view= DetailsItemsuggetionTwoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                  SuggertionBvh(view)

             }

          else -> {
              val view = DetailsAllGridBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                SuggertionAllvh(view)
          }
      }

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
           when(val result=list[position]){

               is DetailsAll_itemmodel.SuggetionA -> {
                   (holder as SuggertionAvh).bind(result.list)
               }
               is DetailsAll_itemmodel.SuggetionB -> {
                   (holder as SuggertionBvh).bind(result.list)
               }
               is DetailsAll_itemmodel.Allproduct -> {
                   (holder as SuggertionAllvh).bind(result.list)
               }

           }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class SuggertionAvh(val binding: DetailItemSuggetiononeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(list: List<Product>) {
            val rv = binding.suggetionRecycler
            rv.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            rv.adapter = HorizontalitemAdapter(list)
            rv.isNestedScrollingEnabled = false
        }
    }

    class SuggertionBvh(val binding: DetailsItemsuggetionTwoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(list: List<Product>) {
            val rv = binding.suggetionRecycler2
            rv.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            rv.adapter = HorizontalitemAdapter(list)
            rv.isNestedScrollingEnabled = false
        }
    }

    class SuggertionAllvh(val binding: DetailsAllGridBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(list: List<Product>) {
            val rv = binding.detailsAllgridrecycler
            rv.layoutManager =
                GridLayoutManager(itemView.context,2)
            rv.adapter = GridAdapter(list)
            rv.isNestedScrollingEnabled = false
        }
    }
}