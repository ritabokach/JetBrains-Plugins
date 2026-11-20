package ru.bokach.plugins.ui.screens.catalog

sealed interface CatalogIntent {
    data class QueryChanged(val query: String) : CatalogIntent
    data class PluginClicked(val pluginId: Int) : CatalogIntent

    /** The list is scrolled close to its end. */
    data object LoadMore : CatalogIntent
    data object Retry : CatalogIntent
}
