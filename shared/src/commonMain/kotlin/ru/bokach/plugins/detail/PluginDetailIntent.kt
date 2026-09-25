package ru.bokach.plugins.detail

sealed interface PluginDetailIntent {
    data object VendorClicked : PluginDetailIntent
}
