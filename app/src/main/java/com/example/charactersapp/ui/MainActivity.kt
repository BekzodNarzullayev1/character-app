package com.example.charactersapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.work.ListenableWorker.Result.retry
import com.example.charactersapp.R
import com.example.charactersapp.adapters.CharactersAdapter
import com.example.charactersapp.adapters.CharactersLoadStateAdapter
import com.example.charactersapp.databinding.ActivityMainBinding
import com.example.charactersapp.models.Character
import com.example.charactersapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri


@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),CharactersAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter :CharactersAdapter
    private var searchJob: Job? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CharactersAdapter(this)

        startSearchJob()
        setUpAdapter()
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }

    @ExperimentalPagingApi
    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchCharacters()
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    @ExperimentalPagingApi
    private fun setUpAdapter() {
        binding.rv.setHasFixedSize(true)

        binding.rv.adapter = adapter.withLoadStateFooter(
            footer = CharactersLoadStateAdapter { retry() }
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {

                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = true
                }
                binding.errorTxt.isVisible = false
                binding.retryBtn.isVisible = false

            } else {
                binding.progress.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        binding.errorTxt.isVisible = true
                        binding.errorTxt.text = it.error.localizedMessage
                        binding.retryBtn.isVisible = true
                        binding.retryBtn.setOnClickListener {
                            retry()
                        }
                    }

                }

            }
        }
    }

    private fun retry() {
        adapter.retry()
    }

    override fun onItemClick(character: Character) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(character.url))
        startActivity(browserIntent)
    }
}