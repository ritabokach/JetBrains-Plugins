package ru.bokach.plugins.ui.screens.detail

sealed interface PluginDetailIntent {
    data object VendorClicked : PluginDetailIntent
}
