package com.example.doi_catalog_cmp

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.doi_catalog_cmp.data.ArticleRepository
import com.example.doi_catalog_cmp.navigation.ArticleDetailRoute
import com.example.doi_catalog_cmp.navigation.ArticleListRoute
import com.example.doi_catalog_cmp.presentation.list.detail.ArticleDetailScreen
import com.example.doi_catalog_cmp.presentation.list.detail.ArticleDetailViewModel
import com.example.doi_catalog_cmp.presentation.list.ArticleListScreen
import com.example.doi_catalog_cmp.presentation.list.ArticleListViewModel
import kotlinx.serialization.modules.polymorphic
import com.example.doi_catalog_cmp.ui.theme.AppTheme

@Composable
fun App() {
    // 1. Конфиг для сериализации (обязательно для Desktop/Web/iOS)
    val config = androidx.savedstate.serialization.SavedStateConfiguration {
        serializersModule = kotlinx.serialization.modules.SerializersModule {
            polymorphic(androidx.navigation3.runtime.NavKey::class) {
                subclass(ArticleListRoute::class, ArticleListRoute.serializer())
                subclass(ArticleDetailRoute::class, ArticleDetailRoute.serializer())
            }
        }
    }

    // 2. Стек навигации
    val backStack = rememberNavBackStack(config, ArticleListRoute)
    var isDarkTheme by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }


    // 3. Один репозиторий для всего приложения
    val repository = ArticleRepository()

    AppTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // 4. Навигация
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                popTransitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                },
                entryProvider = entryProvider {

                    // --- ЭКРАН СПИСКА ---
                    entry<ArticleListRoute> {
                        val viewModel: ArticleListViewModel = viewModel {
                            ArticleListViewModel(repository)
                        }
                        val state by viewModel.state.collectAsState()

                        ArticleListScreen(
                            state = state,
                            onArticleClick = { article ->
                                // Переход на деталь по клику
                                backStack.add(ArticleDetailRoute(article.doi))
                            },
                            onSearch = viewModel::search,
                            onToggleTheme = { isDarkTheme = !isDarkTheme },
                            isDarkTheme = isDarkTheme
                        )
                    }

                    // --- ЭКРАН ДЕТАЛИ ---
                    entry<ArticleDetailRoute> { route ->
                        // Создаем ViewModel для детали, привязывая её к конкретному doi
                        val viewModel: ArticleDetailViewModel = viewModel(key = route.doi) {
                            ArticleDetailViewModel(repository)
                        }

                        // Грузим статью при открытии
                        LaunchedEffect(route.doi) {
                            viewModel.loadArticle(route.doi)
                        }

                        val state by viewModel.state.collectAsState()

                        ArticleDetailScreen(
                            state = state,
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }
                }
            )
        }
    }
}