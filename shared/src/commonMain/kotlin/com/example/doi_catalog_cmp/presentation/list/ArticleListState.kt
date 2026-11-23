package com.example.doi_catalog_cmp.presentation.list

import com.example.doi_catalog_cmp.domain.ArticleModel

sealed interface ArticleListState{
    data object Loading : ArticleListState
    data class Success(
        val articles : List<ArticleModel>,
        val query : String = ""
    ) : ArticleListState
    data object NotFound : ArticleListState
}