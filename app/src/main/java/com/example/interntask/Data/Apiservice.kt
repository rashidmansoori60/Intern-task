package com.example.interntask.Data

import androidx.room.Query
import com.example.interntask.model.Bannermodel.PhotoResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface Apiservice {

    @Headers("Authorization:lWS5Wed5L1I48GBz19qfsxp3QahpEW0lyOB8UXGChhhWSo07gzufqWpz")
    @GET("v1/search")

    suspend fun getPhotobanner(
        @retrofit2.http.Query("query")  query: String="e commerce",
        @retrofit2.http.Query("perPage")  perPage: Int=1
    ): retrofit2.Response<PhotoResponse>

}