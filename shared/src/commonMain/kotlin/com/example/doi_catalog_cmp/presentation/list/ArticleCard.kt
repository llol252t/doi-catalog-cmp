package com.example.doi_catalog_cmp.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doi_catalog_cmp.domain.ArticleModel

@Composable
fun ArticleCard(
    article: ArticleModel, onClick: () -> Unit
){
    Card(modifier = Modifier.fillMaxSize().clickable{onClick()},
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant))
    {
        Column(modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp))
        {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (article.authors.isNotEmpty()) {
                Text(
                    text = article.authors.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
             Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                 Text(
                     text = article.year.toString(),
                     style = MaterialTheme.typography.labelMedium,
                     color = MaterialTheme.colorScheme.primary
                 )
                 Text(
                     text = "· ${article.journal}",
                     style = MaterialTheme.typography.labelMedium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                     maxLines = 1
                 )
             }
        }
    }
}