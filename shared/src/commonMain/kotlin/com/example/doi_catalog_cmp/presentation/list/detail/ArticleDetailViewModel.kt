package com.example.doi_catalog_cmp.presentation.list.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doi_catalog_cmp.core.suspendRunCatching
import com.example.doi_catalog_cmp.domain.ArticleRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ArticleDetailState>(ArticleDetailState.Loading)
    val state: StateFlow<ArticleDetailState> = _state.asStateFlow()

    private var requestJob: Job? = null

    fun loadArticle(doi: String) {
        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            _state.value = ArticleDetailState.Loading

            try {
                val article = repository.getArticleById(doi)
                ensureActive()

                if (article == null) {
                    _state.value = ArticleDetailState.NotFound
                    return@launch
                }

                val relatedDoi = if (article.journal.isNotBlank()) {
                    suspendRunCatching {
                        repository.getRelatedArticle(article.journal, article.doi)?.doi
                    }.getOrNull()
                } else {
                    null
                }

                ensureActive()
                _state.value = ArticleDetailState.Success(article, relatedDoi)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ensureActive()
                _state.value = ArticleDetailState.Error
            }
        }
    }
}
