package com.example.doi_catalog_cmp.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doi_catalog_cmp.domain.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleListViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ArticleListState>(ArticleListState.Loading)
    val state: StateFlow<ArticleListState> = _state.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _state.value = ArticleListState.Loading
            try {
                val articles = repository.getArticles()
                if (articles.isEmpty()) {
                    _state.value = ArticleListState.NotFound
                } else {
                    _state.value = ArticleListState.Success(articles)
                }
            } catch (e: Exception) {
                _state.value = ArticleListState.NotFound
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            try {
                val articles = repository.searchArticles(query)
                if (articles.isEmpty()) {
                    _state.value = ArticleListState.NotFound
                } else {
                    _state.value = ArticleListState.Success(articles, query)
                }
            } catch (e: Exception) {
                _state.value = ArticleListState.NotFound
            }
        }
    }
}