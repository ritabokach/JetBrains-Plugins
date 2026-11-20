package ru.bokach.plugins.ui.screens.vendor

sealed interface VendorIntent {
    data class PluginClicked(val pluginId: Int) : VendorIntent
}
