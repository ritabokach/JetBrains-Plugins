package ru.bokach.plugins.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bokach.plugins.catalog.CatalogIntent
import ru.bokach.plugins.catalog.CatalogState
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
