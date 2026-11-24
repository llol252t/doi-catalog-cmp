package com.example.doi_catalog_cmp.data.network

import com.example.doi_catalog_cmp.domain.ArticleRepository

internal class ArticleRepositorySession(
    val repository: ArticleRepository,
    val close: () -> Unit
)

internal expect fun openArticleRepository(): ArticleRepositorySession

internal fun openLocalArticleRepository(): ArticleRepositorySession {
    val client = createArticleHttpClient()
    return ArticleRepositorySession(CrossrefArticleRepository(ArticleApi(client)), client::close)
}
