package com.example.doi_catalog_cmp.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkResponseDto(
    val message: ArticleDto
)
