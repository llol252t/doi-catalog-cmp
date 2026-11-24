package com.example.doi_catalog_cmp.data.network

import com.example.doi_catalog_cmp.data.network.dto.ArticleDto
import com.example.doi_catalog_cmp.data.network.dto.WorkResponseDto
import com.example.doi_catalog_cmp.data.network.dto.WorksMessageDto
import com.example.doi_catalog_cmp.data.network.dto.WorksResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments

internal class ArticleApi(private val client: HttpClient) {
    suspend fun getWorks(
        query: String? = null,
        journal: String? = null,
        cursor: String = "*",
        rows: Int = 20
    ): WorksMessageDto = client.get("https://api.crossref.org/works") {
        parameter("rows", rows)
        parameter("cursor", cursor)
        query?.let { parameter("query.bibliographic", it) }
        journal?.let { parameter("query.container-title", it) }
    }.body<WorksResponseDto>().message

    suspend fun getWork(doi: String): ArticleDto? =
        try {
            client.get("https://api.crossref.org/works") {
                url { appendPathSegments(doi, encodeSlash = false) }
            }.body<WorkResponseDto>().message
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound) null else throw e
        }
}
