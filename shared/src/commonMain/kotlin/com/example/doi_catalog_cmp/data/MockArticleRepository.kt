package com.example.doi_catalog_cmp.data

import com.example.doi_catalog_cmp.domain.ArticleModel
import com.example.doi_catalog_cmp.domain.ArticleRepository
import kotlinx.coroutines.delay

class MockArticleRepository : ArticleRepository {
    override suspend fun getArticleById(doi: String): ArticleModel? {
        delay(200)
        return mockArticles.find { it.doi == doi }
    }

    override suspend fun getArticles(): List<ArticleModel> {
        delay(300)
        return mockArticles
    }

    override suspend fun searchArticles(query: String): List<ArticleModel> {
        delay(200)
        if (query.isBlank()) return mockArticles
        val q = query.lowercase()
        return mockArticles.filter { article ->
            article.title.lowercase().contains(q) ||
                    article.authors.any { author -> author.lowercase().contains(q) } ||
                    article.journal.lowercase().contains(q)
        }
    }
    override suspend fun getRelatedArticle(journal: String, excludeDoi: String): ArticleModel? {
        return mockArticles.firstOrNull {
            it.journal == journal && it.doi != excludeDoi
        }
    }
}