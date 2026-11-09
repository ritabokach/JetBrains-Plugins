package ru.bokach.plugins.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bokach.plugins.Screen
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.model.toDetail
import ru.bokach.plugins.ui.navigation.Navigator
import ru.bokach.plugins.utils.suspendRunCatching

class PluginDetailViewModel(
    private val pluginId: Int,
    private val navigator: Navigator,
    private val repository: CatalogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PluginDetailState>(PluginDetailState.Loading)
    val state: StateFlow<PluginDetailState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun onIntent(intent: PluginDetailIntent) {
        when (intent) {
            PluginDetailIntent.VendorClicked -> {
                val content = _state.value as? PluginDetailState.Content ?: return
                val vendorSlug = content.plugin.vendorSlug ?: return
                navigator.open(Screen.VendorPage(vendorSlug))
            }
            PluginDetailIntent.Retry -> load()
        }
    }

    private fun load() {
        loadJob?.cancel()
        _state.value = PluginDetailState.Loading
        loadJob = viewModelScope.launch {
            _state.value = suspendRunCatching { repository.getPlugin(pluginId).toDetail() }
                .fold(
                    onSuccess = { PluginDetailState.Content(it) },
                    onFailure = { PluginDetailState.Error },
                )
        }
    }
}
