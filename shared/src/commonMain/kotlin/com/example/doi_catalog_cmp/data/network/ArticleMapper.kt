package com.example.doi_catalog_cmp.data.network

import com.example.doi_catalog_cmp.data.network.dto.ArticleDto
import com.example.doi_catalog_cmp.domain.ArticleModel

private val abstractParagraphEnd = Regex("</(?:[A-Za-z0-9_-]+:)?p\\s*>", RegexOption.IGNORE_CASE)
private val abstractTag = Regex("</?[A-Za-z][^>]*>")

internal fun ArticleDto.toModel(): ArticleModel = ArticleModel(
    doi = doi,
    title = title.firstOrNull { it.isNotBlank() } ?: doi,
    authors = author.mapNotNull { person ->
        person.name?.takeIf { it.isNotBlank() }
              ?: listOfNotNull(person.given, person.family)
                     .filter { it.isNotBlank() }
                     .joinToString(" ")
                     .takeIf { it.isNotBlank() }
        },
    year = issued?.dateParts?.firstOrNull()?.firstOrNull() ?: 0,
    journal = containerTitle.firstOrNull { it.isNotBlank() }.orEmpty(),
    type = type.orEmpty(),
    abstractText = abstractText.orEmpty()
        .replace(abstractParagraphEnd, "\n\n")
        .replace(abstractTag, "")
        .trim()
)
