package ru.bokach.plugins.ui.screens.catalog

sealed interface CatalogIntent {
    data class QueryChanged(val query: String) : CatalogIntent
    data class PluginClicked(val pluginId: Int) : CatalogIntent
}
