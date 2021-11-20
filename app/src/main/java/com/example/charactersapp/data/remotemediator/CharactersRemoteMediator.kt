package com.example.charactersapp.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.charactersapp.api.CharacterApi
import com.example.charactersapp.data.db.AppDatabase
import com.example.charactersapp.data.entity.RemoteKeys
import com.example.charactersapp.models.Character
import com.example.charactersapp.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class CharactersRemoteMediator(private val service: CharacterApi, private val db: AppDatabase):
    RemoteMediator<Int, Character>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (db.charactersDao.count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {

                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getCharacters(page)

            val charactersList = apiResponse.characterList


            val endOfPaginationReached =
                apiResponse.info.next == null || apiResponse.info.prev == "https://rickandmortyapi.com/api/character?page=41"




            db.withTransaction {

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1


                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )
                db.charactersDao.insertMultipleCharacters(charactersList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }

}