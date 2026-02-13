package com.example.interntask.adapters.Searchfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interntask.databinding.SearchsuggetionItemBinding

class Searchadapter(var list: MutableList<String>): RecyclerView.Adapter<Searchadapter.vh>() {


    fun submitlist(newlist: List<String>,isold: Boolean){

        if(isold){
            var startposition=this.list.size
            this.list.addAll(newlist)
            notifyItemRangeInserted(startposition,newlist.size)
        }
        else{
            this.list.clear()
            this.list.addAll(newlist)
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): vh {
        val binding= SearchsuggetionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return vh(binding)
    }

    override fun onBindViewHolder(
        holder: vh,
        position: Int
    ) {
        var item=list[position]
        holder.binding.tvSuggestion.text=item

    }

    override fun getItemCount(): Int {
     return list.size
    }

    class vh(val binding: SearchsuggetionItemBinding): RecyclerView.ViewHolder(binding.root)
}