package com.example.interntask.Uistate

import com.example.interntask.model.MainhomeModel.Product

sealed class Searchstate(){

    data class Recentsearch(val list: List<String>) : Searchstate()
    object Loading : Searchstate()
    data class Error(val error:String): Searchstate()
    data class Livesearch(val list: List<String>) : Searchstate()
    data class Resultsproduct(val products: List<Product>) : Searchstate()
}