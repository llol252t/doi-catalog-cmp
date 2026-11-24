package com.example.doi_catalog_cmp.data.network

import com.example.doi_catalog_cmp.domain.ArticleModel
import com.example.doi_catalog_cmp.domain.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

internal class CrossrefArticleRepository(private val api: ArticleApi) : ArticleRepository {

    override suspend fun getArticles(): List<ArticleModel> = loadWorks(query = null)
    override suspend fun searchArticles(query: String): List<ArticleModel> =
        loadWorks(query.trim().takeIf { it.isNotEmpty() })

    override suspend fun getArticleById(doi: String): ArticleModel? =
        withContext(Dispatchers.Default) {
            api.getWork(doi)?.toModel()
        }
    override suspend fun getRelatedArticle(
        journal: String,
        excludeDoi: String
    ): ArticleModel? = withContext(Dispatchers.Default) {
        if (journal.isBlank()) return@withContext null

        api.getWorks(journal = journal).items.firstOrNull { candidate ->
            !candidate.doi.equals(excludeDoi, ignoreCase = true) &&
                    candidate.containerTitle.any { title ->
                        title.trim().equals(journal.trim(), ignoreCase = true)
                    }
        }?.toModel()
    }

    private suspend fun loadWorks(query: String?): List<ArticleModel> = withContext(Dispatchers.Default) {
        val rows = 20
        val articles = linkedMapOf<String, ArticleModel>()
        var cursor = "*"

        repeat(2) {
            val page = api.getWorks(query = query, cursor = cursor, rows = rows)
            for (dto in page.items) {
                ensureActive()
                articles[dto.doi.lowercase()] = dto.toModel()
            }

            if (page.items.size < rows) {
                return@withContext articles.values.toList()
            }

            cursor = page.nextCursor ?: return@withContext articles.values.toList()
        }

        articles.values.toList()
    }

}
