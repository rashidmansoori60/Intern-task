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
import dagger.hilt.android.internal.Contexts

class DetailSuggetionAdapter(var list: MutableList<DetailsAll_itemmodel>,val loadmore:()-> Unit,val onclick: (Int)-> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var vh:SuggertionAllvh?=null

    fun update(list: MutableList<DetailsAll_itemmodel>){
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
                    SuggertionAvh(view,onclick)

            }
             1->{
              val view= DetailsItemsuggetionTwoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                  SuggertionBvh(view, onclick)

             }

          else -> {
              val view = DetailsAllGridBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                SuggertionAllvh(view, loadmore )
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
                   val vhh=holder as SuggertionAllvh
                   vh=vhh
                   vhh.bind(result.list)
               }

           }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class SuggertionAvh(val binding: DetailItemSuggetiononeBinding,val onclick: (Int)-> Unit ): RecyclerView.ViewHolder(binding.root){
        val rv = binding.suggetionRecycler
        val adapterA= HorizontalitemAdapter(mutableListOf()){it->
            onclick(it)
        }



        init {
            rv.apply {
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter =adapterA
                isNestedScrollingEnabled = false
            }
        }
        fun bind(list: List<Product>) {
            adapterA.setInitialData(list)
        }
    }

    class SuggertionBvh(val binding: DetailsItemsuggetionTwoBinding,val onclick: (Int)-> Unit): RecyclerView.ViewHolder(binding.root){
        val rv = binding.suggetionRecycler2
        val adapterB = HorizontalitemAdapter(mutableListOf()){it->
            onclick(it)
        }

        init {
            rv.apply {
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter =adapterB
                isNestedScrollingEnabled = false
            }
        }
        fun bind(list: List<Product>) {
            adapterB.setInitialData(list)
        }
    }

    class SuggertionAllvh(val binding: DetailsAllGridBinding,val onLoadMore: () -> Unit): RecyclerView.ViewHolder(binding.root){
        val rv = binding.detailsAllgridrecycler

        private var isLoading = false
        val lm=GridLayoutManager(itemView.context, 2)
        val adapterrv = GridAdapter(mutableListOf())

        init {


            rv.apply {
                    adapter = adapterrv
                    layoutManager=lm
                    isNestedScrollingEnabled = false


                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                        val visible = lm.childCount
                        val total = lm.itemCount
                        val first = lm.findFirstVisibleItemPosition()

                        if (!isLoading && visible + first >= total - 2) {
                            isLoading = true
                            onLoadMore()
                        }
                    }
                })
            }
        }
        fun bind(list: List<Product>) {
           adapterrv.setInitialData(list)
        }
        fun addMore(list: List<Product>) {
            adapterrv.addMoreItems(list)
            isLoading = false
        }
    }
    fun addmoreGriditem(list: List<Product>){
       vh?.addMore(list)
    }
}