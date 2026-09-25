package ru.bokach.plugins.catalog

sealed interface CatalogIntent {
    data class QueryChanged(val value: String) : CatalogIntent
    data class PluginClicked(val pluginId: Int) : CatalogIntent
}
