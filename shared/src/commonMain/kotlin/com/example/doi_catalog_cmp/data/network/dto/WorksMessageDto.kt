package com.example.doi_catalog_cmp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorksMessageDto(
    val items: List<ArticleDto>,
    @SerialName("next-cursor")
    val nextCursor: String? = null
)
