package ru.bokach.plugins.vendor

sealed interface VendorIntent {
    data class PluginClicked(val pluginId: Int) : VendorIntent
}
