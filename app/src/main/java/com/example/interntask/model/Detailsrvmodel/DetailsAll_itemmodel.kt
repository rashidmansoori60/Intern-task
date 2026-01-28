package com.example.interntask.model.Detailsrvmodel

import com.example.interntask.model.MainhomeModel.Product

sealed class DetailsAll_itemmodel {

    data class SuggetionA(val list:List<Product>): DetailsAll_itemmodel()

    data class SuggetionB(val list: List<Product>): DetailsAll_itemmodel()

    data class Allproduct(val list: List<Product>): DetailsAll_itemmodel()

}