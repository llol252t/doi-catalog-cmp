package com.example.doi_catalog_cmp.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doi_catalog_cmp.domain.ArticleRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ArticleListViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    private var currentQuery = ""
    private var requestJob: Job? = null
    private val _state = MutableStateFlow<ArticleListState>(ArticleListState.Loading)
    val state: StateFlow<ArticleListState> = _state.asStateFlow()

    init {
        load("")
    }

    fun search(query: String) = load(query)

    fun retry() = load(currentQuery)

    private fun load(query: String) {
        currentQuery = query
        requestJob?.cancel()

        requestJob = viewModelScope.launch {
            _state.value = ArticleListState.Loading

            try {
                val articles = if (query.isBlank()) {
                    repository.getArticles()
                } else {
                    repository.searchArticles(query)
                }

                ensureActive()
                _state.value = if (articles.isEmpty()) {
                    ArticleListState.NotFound
                } else {
                    ArticleListState.Success(articles, query)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ensureActive()
                _state.value = ArticleListState.Error
            }
        }
    }
}
