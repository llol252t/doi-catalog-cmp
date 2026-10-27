package com.example.doi_catalog_cmp.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doi_catalog_cmp.domain.ArticleModel
import com.example.doi_catalog_cmp.presentation.list.ArticleCard
import doi_catalog_cmp.shared.generated.resources.Res
import doi_catalog_cmp.shared.generated.resources.app_title
import doi_catalog_cmp.shared.generated.resources.nothing_found
import doi_catalog_cmp.shared.generated.resources.search_hint
import org.jetbrains.compose.resources.stringResource

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
                title = { Text(stringResource(Res.string.app_title)) },
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
                placeholder = { Text(stringResource(Res.string.search_hint)) },
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
                        Text(stringResource(Res.string.nothing_found))
                    }
                }
            }
        }
    }
}