package ru.bokach.plugins

sealed interface Screen {
    data object Catalog : Screen
    data class PluginDetail(val pluginId: Int) : Screen
    data class VendorPage(val vendorId: Int) : Screen
}
