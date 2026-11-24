package com.example.doi_catalog_cmp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal expect fun createPlatformHttpClient(
    configure: HttpClientConfig<*>.() -> Unit
): HttpClient

internal fun createArticleHttpClient(): HttpClient = createPlatformHttpClient {
    expectSuccess = true
    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.INFO
    }
    install(ContentNegotiation) {
        val parser = Json { ignoreUnknownKeys = true }
        json(parser)
        json(parser, ContentType.parse("application/vnd.crossref-api-message+json"))
    }
}
