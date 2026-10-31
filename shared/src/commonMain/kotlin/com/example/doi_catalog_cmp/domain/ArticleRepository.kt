package com.example.doi_catalog_cmp.domain

interface ArticleRepository {
    suspend fun getArticles(): List<ArticleModel>
    suspend fun getArticleById(doi: String): ArticleModel?
    suspend fun searchArticles(query: String): List<ArticleModel>

    suspend fun  getRelatedArticle(journal: String, excludeDoi: String) : ArticleModel?
}