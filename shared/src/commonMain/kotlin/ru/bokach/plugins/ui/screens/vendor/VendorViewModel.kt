package ru.bokach.plugins.ui.screens.vendor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bokach.plugins.Screen
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.model.toCards
import ru.bokach.plugins.ui.model.toUi
import ru.bokach.plugins.ui.navigation.Navigator
import ru.bokach.plugins.utils.suspendRunCatching

class VendorViewModel(
    private val vendorSlug: String,
    private val navigator: Navigator,
    private val repository: CatalogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<VendorState>(VendorState.Loading)
    val state: StateFlow<VendorState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun onIntent(intent: VendorIntent) {
        when (intent) {
            is VendorIntent.PluginClicked ->
                navigator.open(Screen.PluginDetail(intent.pluginId))
            VendorIntent.Retry -> load()
        }
    }

    /** The plugin search filters by the public name, which only the vendor request knows. */
    private fun load() {
        loadJob?.cancel()
        _state.value = VendorState.Loading
        loadJob = viewModelScope.launch {
            _state.value = suspendRunCatching {
                val vendor = repository.getVendor(vendorSlug)
                val plugins = repository.getPluginsByVendor(vendor.name)
                VendorState.Content(
                    vendor = vendor.toUi(),
                    plugins = plugins.items.toCards(),
                    totalPlugins = plugins.total,
                )
            }.getOrElse { VendorState.Error }
        }
    }
}
