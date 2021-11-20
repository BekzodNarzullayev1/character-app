package com.example.charactersapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.charactersapp.api.CharacterApi
import com.example.charactersapp.data.db.AppDatabase
import com.example.charactersapp.data.remotemediator.CharactersRemoteMediator
import com.example.charactersapp.models.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharactersRepository @Inject constructor(
    private val characterApi: CharacterApi,
    private val db: AppDatabase
) {

    private val pagingSourceFactory = { db.charactersDao.getCharacters() }

    @ExperimentalPagingApi
    fun getPlayers(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CharactersRemoteMediator(
                characterApi,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}