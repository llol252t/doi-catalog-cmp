package com.example.doi_catalog_cmp.data.network

import com.example.doi_catalog_cmp.domain.ArticleModel
import com.example.doi_catalog_cmp.domain.ArticleRepository
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal actual fun openArticleRepository(): ArticleRepositorySession {
    val repository = BrowserArticleRepository()
    return ArticleRepositorySession(repository, repository::close)
}

private class BrowserArticleRepository : ArticleRepository {
    private val pending = mutableMapOf<ArticleWorker, CancellableContinuation<List<ArticleModel>>>()
    private var closed = false

    override suspend fun getArticles(): List<ArticleModel> = request("list")

    override suspend fun searchArticles(query: String): List<ArticleModel> =
        request("search", query)

    override suspend fun getArticleById(doi: String): ArticleModel? =
        request("detail", doi).firstOrNull()

    override suspend fun getRelatedArticle(journal: String, excludeDoi: String): ArticleModel? =
        request("related", journal, excludeDoi).firstOrNull()

    fun close() {
        closed = true
        val requests = pending.toMap()
        pending.clear()
        for ((worker, continuation) in requests) {
            worker.terminate()
            continuation.cancel()
        }
    }

    private suspend fun request(
        operation: String,
        value: String = "",
        excludeDoi: String = ""
    ): List<ArticleModel> = withTimeoutOrNull(40_000) {
        suspendCancellableCoroutine { continuation ->
            if (closed) {
                continuation.cancel()
                return@suspendCancellableCoroutine
            }
            val worker = startArticleWorker(
                operation,
                value,
                excludeDoi,
                onSuccess = { sender, articles ->
                    if (pending.remove(sender) != null) {
                        sender.terminate()
                        try {
                            val models = List(articles.length) { index -> articles.at(index).toModel() }
                            continuation.resume(models)
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                },
                onFailure = { sender, message ->
                    if (pending.remove(sender) != null) {
                        sender.terminate()
                        continuation.resumeWithException(IllegalStateException(message))
                    }
                }
            )
            pending[worker] = continuation
            continuation.invokeOnCancellation {
                pending.remove(worker)
                worker.terminate()
            }
        }
    } ?: error("Article worker timed out")
}

private external interface ArticleWorker {
    fun terminate()
}

private external interface WorkerArticles {
    val length: Int
    fun at(index: Int): WorkerArticle
}

private external interface WorkerAuthors {
    val length: Int
    fun at(index: Int): String
}

private external interface WorkerArticle {
    val doi: String
    val title: String
    val authors: WorkerAuthors
    val year: Int
    val journal: String
    val type: String
    val abstractText: String
}

private fun WorkerArticle.toModel() = ArticleModel(
    doi, title, List(authors.length) { authors.at(it) }, year, journal, type, abstractText
)

@OptIn(ExperimentalWasmJsInterop::class)
private fun startArticleWorker(
    operation: String,
    value: String,
    excludeDoi: String,
    onSuccess: (ArticleWorker, WorkerArticles) -> Unit,
    onFailure: (ArticleWorker, String) -> Unit
): ArticleWorker = js("""
    (() => {
        const worker = new Worker(new URL("article-worker/crossref-worker.js", document.baseURI));
        worker.onmessage = ({ data }) => {
            if (data.error != null) onFailure(worker, data.error);
            else onSuccess(worker, data.articles);
        };
        worker.onerror = (event) => onFailure(worker, event.message || "Article worker failed");
        worker.onmessageerror = () => onFailure(worker, "Invalid article worker reply");
        worker.postMessage({ operation, value, excludeDoi });
        return worker;
    })()
""")
