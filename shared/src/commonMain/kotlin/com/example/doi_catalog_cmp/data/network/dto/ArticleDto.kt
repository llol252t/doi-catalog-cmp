package com.example.doi_catalog_cmp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("DOI")
    val doi: String,
    val title: List<String> = emptyList(),
    val author: List<AuthorDto> = emptyList(),
    val issued: IssuedDto? = null,
    @SerialName("container-title")
    val containerTitle: List<String> = emptyList(),
    val type: String? = null,
    @SerialName("abstract")
    val abstractText: String? = null
)
