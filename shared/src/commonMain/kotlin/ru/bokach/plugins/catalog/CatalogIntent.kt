package ru.bokach.plugins.catalog

sealed interface CatalogIntent {
    data class PluginClicked(val pluginId: Int) : CatalogIntent
}
