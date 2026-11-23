package com.example.doi_catalog_cmp.domain

data class ArticleModel(
    val doi: String,
    val title: String,
    val authors: List<String>,
    val year: Int,
    val journal: String,
    val type: String,
    val abstractText: String
)