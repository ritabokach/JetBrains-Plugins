package ru.bokach.plugins.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bokach.plugins.Screen
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.model.PluginDetailUi
import ru.bokach.plugins.ui.model.toDetail
import ru.bokach.plugins.ui.navigation.Navigator

class PluginDetailViewModel(
    pluginId: Int,
    private val navigator: Navigator,
    private val repository: CatalogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PluginDetailUi?>(null)
    val state: StateFlow<PluginDetailUi?> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = repository.getPlugin(pluginId).toDetail()
        }
    }

    fun onIntent(intent: PluginDetailIntent) {
        when (intent) {
            PluginDetailIntent.VendorClicked -> {
                val vendorSlug = _state.value?.vendorSlug ?: return
                navigator.open(Screen.VendorPage(vendorSlug))
            }
        }
    }
}
