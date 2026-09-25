package ru.bokach.plugins.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.catalog.CatalogIntent
import ru.bokach.plugins.catalog.CatalogState
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.catalog_empty
import ru.bokach.plugins.resources.catalog_search_hint
import ru.bokach.plugins.ui.components.PluginCard

@Composable
fun CatalogScreen(
    state: CatalogState,
    onIntent: (CatalogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { onIntent(CatalogIntent.QueryChanged(it)) },
            label = { Text(stringResource(Res.string.catalog_search_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        )
        if (state.items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(Res.string.catalog_empty, state.query),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp),
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.items, key = { it.id }) { plugin ->
                    PluginCard(
                        plugin = plugin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onIntent(CatalogIntent.PluginClicked(plugin.id)) },
                    )
                }
            }
        }
    }
}
