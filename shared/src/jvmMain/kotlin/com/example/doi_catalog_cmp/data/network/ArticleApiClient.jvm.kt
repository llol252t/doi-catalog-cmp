package com.example.doi_catalog_cmp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

internal actual fun createPlatformHttpClient(
    configure: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(CIO) { configure(this) }

internal actual fun openArticleRepository(): ArticleRepositorySession = openLocalArticleRepository()
