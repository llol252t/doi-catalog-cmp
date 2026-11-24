package com.example.doi_catalog_cmp.data.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

fun main() {
    val scope = CoroutineScope(Dispatchers.Default)
    val session = openLocalArticleRepository()
    val repository = session.repository

    listenForArticleRequest { operation, value, excludeDoi, reply, fail ->
        scope.launch {
            try {
                val articles = when (operation) {
                    "list" -> repository.getArticles()
                    "search" -> repository.searchArticles(value)
                    "detail" -> listOfNotNull(repository.getArticleById(value))
                    "related" -> listOfNotNull(repository.getRelatedArticle(value, excludeDoi))
                    else -> error("Unknown article request")
                }
                reply(Json.encodeToString(articles))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                fail(e.message ?: "Article request failed")
            } finally {
                session.close()
            }
        }
    }
}

private fun listenForArticleRequest(
    handler: (String, String, String, (String) -> Unit, (String) -> Unit) -> Unit
) {
    js("""
        self.addEventListener("message", function (event) {
            const { operation, value, excludeDoi } = event.data;
            handler(operation, value, excludeDoi,
                function (json) { self.postMessage({ articles: JSON.parse(json) }); },
                function (error) { self.postMessage({ error: error }); });
        }, { once: true });
    """)
}
