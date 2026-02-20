package com.example.interntask.Data.Local.Search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queries")
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    val query:String
)