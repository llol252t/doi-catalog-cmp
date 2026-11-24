package com.example.doi_catalog_cmp.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val given: String? = null,
    val family: String? = null,
    val name: String? = null
)
