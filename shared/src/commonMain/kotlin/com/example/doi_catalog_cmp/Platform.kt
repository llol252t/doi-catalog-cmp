package com.example.doi_catalog_cmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform