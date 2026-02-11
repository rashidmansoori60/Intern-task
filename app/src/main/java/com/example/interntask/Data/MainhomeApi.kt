package com.example.interntask.Data
import com.example.interntask.model.MainhomeModel.Product
import com.example.interntask.model.MainhomeModel.ProductResponse
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MainhomeApi{



        @GET("products")
        suspend fun getProducts(
            @Query("limit") limit: Int,
            @Query("skip") skip: Int
        ):retrofit2.Response<ProductResponse>



    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category:String= "groceries"
    ):retrofit2.Response<ProductResponse>


    @GET("products/category/{category}")
    suspend fun getProductsCategorygrid(
        @Path("category") category: String,
        @Query("limit") limit: Int = 20
    ):retrofit2.Response<ProductResponse>



    @GET("products/category/{category}")
    suspend fun getProductsCategory_detailsuggetion(
        @Path("category") category: String
    ):retrofit2.Response<ProductResponse>



        @GET("products/{id}")
        suspend fun getProductById(
            @Path("id") id: Int
        ): Product?


    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): retrofit2.Response<ProductResponse>

}

