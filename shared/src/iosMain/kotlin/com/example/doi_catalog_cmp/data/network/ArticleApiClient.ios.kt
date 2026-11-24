package com.example.doi_catalog_cmp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

internal actual fun createPlatformHttpClient(
    configure: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(Darwin) { configure(this) }

internal actual fun openArticleRepository(): ArticleRepositorySession = openLocalArticleRepository()
