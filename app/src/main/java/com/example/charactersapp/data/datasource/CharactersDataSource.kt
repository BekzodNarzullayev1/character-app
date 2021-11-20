package com.example.charactersapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.charactersapp.api.CharacterApi
import com.example.charactersapp.models.Character
import com.example.charactersapp.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class CharactersDataSource(private val characterApi: CharacterApi) :
    PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = characterApi.getCharacters(page)
            val characters = response.characterList
            LoadResult.Page(
                data = characters,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (characters.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }
}