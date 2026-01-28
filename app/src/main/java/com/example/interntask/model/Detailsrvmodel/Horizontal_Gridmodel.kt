package com.example.interntask.model.Detailsrvmodel

import com.example.interntask.model.MainhomeModel.Product

sealed class Horizontal_Gridmodel {

    data class Horizontal(val list: List<Product>):Horizontal_Gridmodel()

    data class Grid(val list: List<Product>):Horizontal_Gridmodel()

}
