package com.example.doi_catalog_cmp.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doi_catalog_cmp.domain.ArticleModel
import com.example.doi_catalog_cmp.presentation.list.ArticleCard

@Composable
fun ArticleListScreen(
    state: ArticleListState,
    onArticleClick: (ArticleModel) -> Unit,
    onSearch: (String) -> Unit,
    onToggleTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is ArticleListState.Success && state.query != searchText) {
            searchText = state.query
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статьи") },
                actions = {
                    TextButton(onClick = onToggleTheme) {
                        Text(if (isDarkTheme) "☀️" else "🌙")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onSearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Поиск...") },
                singleLine = true
            )

            when (state) {
                is ArticleListState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ArticleListState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.articles) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    }
                }
                is ArticleListState.NotFound -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ничего не найдено")
                    }
                }
            }
        }
    }
}