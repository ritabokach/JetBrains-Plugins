package ru.bokach.plugins.ui.screens.detail

import ru.bokach.plugins.ui.model.PluginDetailUi

sealed interface PluginDetailState {
    data object Loading : PluginDetailState
    data object Error : PluginDetailState
    data class Content(val plugin: PluginDetailUi) : PluginDetailState
}
