package com.example.interntask.Data.Repository

import android.util.Log
import com.example.interntask.Data.MainhomeApi
import com.example.interntask.model.MainhomeModel.Product
import com.example.interntask.model.MainhomeModel.ProductResponse
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.Response

class MainhomeRepo @Inject constructor(val api: MainhomeApi) {

   private val listofCetegories= listOf(
        "beauty",
        "fragrances",
        "furniture",
        "groceries",
        "home-decoration",
        "kitchen-accessories",
        "laptops",
        "mens-shirts",
        "mens-shoes",
        "mens-watches",
        "mobile-accessories",
        "motorcycle",
        "skin-care",
        "smartphones",
        "sports-accessories",
        "sunglasses",
        "tablets",
        "tops",
        "vehicle",
        "womens-bags",
        "womens-dresses",
        "womens-jewellery",
        "womens-shoes",
        "womens-watches")

    suspend fun getProduct(limit:Int,skip: Int): List<Product>{
        val response=api.getProducts(limit,skip)
        if(response.isSuccessful&&response.body()!=null){
        return response.body()!!.products
        }
        return emptyList()
    }
    suspend fun getSpecial(): List<Product>{
        val response=api.getProductsByCategory("groceries")
        if(response.isSuccessful&&response.body()!=null){
            return response.body()!!.products.take(50)
        }
        return emptyList()
    }
    private val dispatcher = Dispatchers.IO.limitedParallelism(5)
    var currenindexofallmixitem = 0

    suspend fun getAllgrid(): List<Product> {

        if (currenindexofallmixitem >= listofCetegories.size) {
            currenindexofallmixitem = 0
        }

        val category = listofCetegories[currenindexofallmixitem]
        currenindexofallmixitem++

        return try {
            api.getProductsCategorygrid(category)
                .body()?.products
                ?.take(20)
                .orEmpty()
        } catch (e: Exception) {
            Log.e("HomeRepo", "Failed category: $category", e)
            emptyList()
        }
    }

    suspend fun getdetailsuggetion(cetegory:String): List<Product>{
       return api.getProductsCategory_detailsuggetion(cetegory).body()!!.products
    }

    suspend fun getproductbyId(id: Int): Product?{

        return api.getProductById(id)
    }

}