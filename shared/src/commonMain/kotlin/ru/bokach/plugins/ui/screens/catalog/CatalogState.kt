package ru.bokach.plugins.ui.screens.catalog

import ru.bokach.plugins.ui.model.LoadStatus
import ru.bokach.plugins.ui.model.PluginCardUi

/**
 * [status] is the first page of the current query, [appendStatus] is the page
 * being added at the bottom. [endReached] means there is nothing left to load.
 */
data class CatalogState(
    val query: String = "",
    val items: List<PluginCardUi> = emptyList(),
    val status: LoadStatus = LoadStatus.Loading,
    val appendStatus: LoadStatus = LoadStatus.Idle,
    val endReached: Boolean = false,
)
