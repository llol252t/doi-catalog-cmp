package com.example.doi_catalog_cmp

import java.util.Locale

actual fun setAppLocale(language: String) {
    Locale.setDefault(Locale(language))
}