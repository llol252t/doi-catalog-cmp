package com.example.doi_catalog_cmp

import java.util.Locale
import java.util.ResourceBundle

actual fun setAppLocale(language: String) {
    Locale.setDefault(Locale(language))
    ResourceBundle.clearCache()
}