package com.example.doi_catalog_cmp.presentation.list.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doi_catalog_cmp.domain.ArticleModel
import com.example.doi_catalog_cmp.domain.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val repository: ArticleRepository
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

    fun onRelatedArticleClicked(currentArticle: ArticleModel, onNavigate: (String) -> Unit) {
        viewModelScope.launch {
            val related = repository.getRelatedArticle(currentArticle.journal, currentArticle.doi)
            if (related != null) {
                onNavigate(related.doi)
            }
        }
    }
}