package com.example.doi_catalog_cmp.data.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssuedDto(
    @SerialName("date-parts")
    val dateParts: List<List<Int?>> = emptyList()
)
