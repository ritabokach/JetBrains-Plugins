package ru.bokach.plugins.ui.screens.catalog

import ru.bokach.plugins.ui.model.PluginCardUi

data class CatalogState(
    val query: String = "",
    val items: List<PluginCardUi> = emptyList(),
)
