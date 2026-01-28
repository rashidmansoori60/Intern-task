package com.example.interntask.helper

import com.example.interntask.Data.Apiservice
import com.example.interntask.Data.MainhomeApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideApi(): Apiservice{
       return Retrofit.Builder().baseUrl("https://api.pexels.com/").addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(Apiservice::class.java)
    }
    @Provides
    @Singleton
    fun provideMainhomeapi(): MainhomeApi{
        return Retrofit.Builder().baseUrl("https://dummyjson.com/").addConverterFactory(
            GsonConverterFactory.create()).build().create(MainhomeApi::class.java)
    }

}