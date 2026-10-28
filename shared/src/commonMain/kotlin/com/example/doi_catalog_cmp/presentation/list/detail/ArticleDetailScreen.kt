package com.example.doi_catalog_cmp.presentation.list.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArticleDetailScreen(
    state: ArticleDetailState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статья") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (state) {
            is ArticleDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ArticleDetailState.Success -> {
                val article = state.article
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    if (article.authors.isNotEmpty()) {
                        Text(
                            text = article.authors.joinToString(", "),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "${article.year} · ${article.journal}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = article.type,
                        style = MaterialTheme.typography.labelSmall
                    )
                    if (article.abstractText.isNotEmpty()) {
                        Text(
                            text = article.abstractText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            is ArticleDetailState.NotFound -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Статья не найдена")
                }
            }
        }
    }
}