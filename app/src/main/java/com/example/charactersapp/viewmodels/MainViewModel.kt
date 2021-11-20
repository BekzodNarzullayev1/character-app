package com.example.charactersapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.charactersapp.data.repository.CharactersRepository
import com.example.charactersapp.models.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CharactersRepository
):ViewModel() {

    private var currentResult: Flow<PagingData<Character>>? = null

    @ExperimentalPagingApi
    fun searchCharacters(): Flow<PagingData<Character>> {
        val newResult: Flow<PagingData<Character>> =
            repository.getPlayers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}