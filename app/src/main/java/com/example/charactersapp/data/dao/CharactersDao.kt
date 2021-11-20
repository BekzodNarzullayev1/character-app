package com.example.charactersapp.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.charactersapp.models.Character

@Dao
interface CharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleCharacters(list: List<Character>)

    @Query("SELECT * FROM characters_table")
    fun getCharacters(): PagingSource<Int, Character>

    @Query("DELETE FROM characters_table")
    suspend fun clearRepos()

    @Query("SELECT COUNT(id) from characters_table")
    suspend fun count(): Int

}