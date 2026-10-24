package com.example.doi_catalog_cmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Doi_catalog_cmp",
    ) {
        App()
    }
}