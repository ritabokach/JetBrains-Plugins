package ru.bokach.plugins.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.ic_close
import ru.bokach.plugins.resources.ic_search
import ru.bokach.plugins.resources.search_clear
import ru.bokach.plugins.resources.search_empty
import ru.bokach.plugins.resources.search_hint
import ru.bokach.plugins.ui.components.PluginCard

@Composable
fun CatalogScreen(
    state: CatalogState,
    onIntent: (CatalogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "search") {
            SearchField(
                query = state.query,
                onQueryChange = { onIntent(CatalogIntent.QueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (state.items.isEmpty() && state.query.isNotBlank()) {
            item(key = "empty") {
                Text(
                    text = stringResource(Res.string.search_empty, state.query.trim()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                )
            }
        }
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

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        placeholder = { Text(stringResource(Res.string.search_hint)) },
        leadingIcon = {
            Icon(painter = painterResource(Res.drawable.ic_search), contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.search_clear),
                    )
                }
            }
        },
    )
}
