package com.example.doi_catalog_cmp.presentation.list.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doi_catalog_cmp.domain.IArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val repository: IArticleRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ArticleDetailState>(ArticleDetailState.Loading)
    val state: StateFlow<ArticleDetailState> = _state.asStateFlow()

    fun loadArticle(doi: String) {
        viewModelScope.launch {
            _state.value = ArticleDetailState.Loading
            try {
                val article = repository.getArticleById(doi)
                if (article == null) {
                    _state.value = ArticleDetailState.NotFound
                } else {
                    _state.value = ArticleDetailState.Success(article)
                }
            } catch (e: Exception) {
                _state.value = ArticleDetailState.NotFound
            }
        }
    }
}