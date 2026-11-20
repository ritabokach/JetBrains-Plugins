package ru.bokach.plugins.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.ic_close
import ru.bokach.plugins.resources.ic_search
import ru.bokach.plugins.resources.search_clear
import ru.bokach.plugins.resources.search_empty
import ru.bokach.plugins.resources.search_hint
import ru.bokach.plugins.ui.components.ErrorMessage
import ru.bokach.plugins.ui.components.LoadingIndicator
import ru.bokach.plugins.ui.components.PluginCard
import ru.bokach.plugins.ui.model.LoadStatus

/** How many rows before the end of the list the next page is requested. */
private const val PREFETCH_DISTANCE = 5

@Composable
fun CatalogScreen(
    state: CatalogState,
    onIntent: (CatalogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val currentOnIntent by rememberUpdatedState(onIntent)
    LaunchedEffect(listState) {
        // Re-emits when the list grows, so a short page that leaves the end in
        // view asks for the next one too. The view model drops extra requests.
        snapshotFlow {
            val layout = listState.layoutInfo
            val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible to layout.totalItemsCount
        }
            .distinctUntilChanged()
            .filter { (lastVisible, total) -> lastVisible >= total - PREFETCH_DISTANCE }
            .collect { currentOnIntent(CatalogIntent.LoadMore) }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
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
        when (state.status) {
            LoadStatus.Loading -> item(key = "loading") {
                LoadingIndicator(Modifier.fillMaxWidth())
            }
            LoadStatus.Error -> item(key = "error") {
                ErrorMessage(
                    onRetry = { onIntent(CatalogIntent.Retry) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            LoadStatus.Idle -> Unit
        }
        if (state.status == LoadStatus.Idle && state.items.isEmpty() && state.query.isNotBlank()) {
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
        when (state.appendStatus) {
            LoadStatus.Loading -> item(key = "append-loading") {
                LoadingIndicator(Modifier.fillMaxWidth())
            }
            LoadStatus.Error -> item(key = "append-error") {
                ErrorMessage(
                    onRetry = { onIntent(CatalogIntent.Retry) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            LoadStatus.Idle -> Unit
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
