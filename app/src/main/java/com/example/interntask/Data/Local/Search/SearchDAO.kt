package com.example.interntask.Data.Local.Search

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDAO {
    @Insert
    suspend fun insert(query: SearchEntity)

    @Query("SELECT * FROM queries ORDER BY id DESC ")
     fun getallsearch(): Flow<List<SearchEntity>>

    @Query("DELETE FROM queries WHERE `query` =:text")
    suspend fun deleteByQuery(text: String)
}