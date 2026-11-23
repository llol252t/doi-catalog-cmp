package com.example.doi_catalog_cmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.doi_catalog_cmp.desktop.resources.Res
import com.example.doi_catalog_cmp.desktop.resources.window_title
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.window_title),
    ) {
        App()
    }
}