package com.example.charactersapp.api

import com.example.charactersapp.models.Characters
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int?
    ): Characters
}