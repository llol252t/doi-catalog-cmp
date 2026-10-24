package com.example.doi_catalog_cmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doi_catalog_cmp.data.ArticleRepository
import com.example.doi_catalog_cmp.presentation.list.ArticleListScreen
import com.example.doi_catalog_cmp.presentation.list.ArticleListViewModel

@Composable
fun App() {
    // Создаём ViewModel (Compose сам управляет её жизненным циклом)
    val viewModel: ArticleListViewModel = viewModel {
        ArticleListViewModel(ArticleRepository())
    }

    // Подписываемся на state из ViewModel
    val state by viewModel.state.collectAsState()

    // Заглушка для темы (потом вынесем в отдельный класс)
    val isDarkTheme = false

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ArticleListScreen(
                state = state,
                onArticleClick = { article ->
                    // TODO: навигация на деталь (сделаем позже)
                },
                onSearch = viewModel::search,
                onToggleTheme = {
                    // TODO: переключение темы (сделаем позже)
                },
                isDarkTheme = isDarkTheme
            )
        }
    }
}