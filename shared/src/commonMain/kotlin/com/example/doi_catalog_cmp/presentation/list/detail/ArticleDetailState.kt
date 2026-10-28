package com.example.doi_catalog_cmp.presentation.list.detail

import com.example.doi_catalog_cmp.domain.ArticleModel

sealed interface ArticleDetailState {
    data object Loading : ArticleDetailState
    data class Success(val article: ArticleModel) : ArticleDetailState
    data object NotFound : ArticleDetailState
}