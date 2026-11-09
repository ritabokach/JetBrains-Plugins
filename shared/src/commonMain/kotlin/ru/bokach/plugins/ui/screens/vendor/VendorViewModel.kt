package ru.bokach.plugins.ui.screens.vendor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bokach.plugins.Screen
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.model.toCards
import ru.bokach.plugins.ui.model.toUi
import ru.bokach.plugins.ui.navigation.Navigator

class VendorViewModel(
    vendorSlug: String,
    private val navigator: Navigator,
    private val repository: CatalogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(VendorState())
    val state: StateFlow<VendorState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val vendor = repository.getVendor(vendorSlug)
            val plugins = repository.getPluginsByVendor(vendor.name)
            _state.value = VendorState(
                vendor = vendor.toUi(),
                plugins = plugins.items.toCards(),
            )
        }
    }

    fun onIntent(intent: VendorIntent) {
        when (intent) {
            is VendorIntent.PluginClicked ->
                navigator.open(Screen.PluginDetail(intent.pluginId))
        }
    }
}
