package ru.bokach.plugins.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bokach.plugins.Screen
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.model.toCards
import ru.bokach.plugins.ui.navigation.Navigator

class CatalogViewModel(
    private val navigator: Navigator,
    private val repository: CatalogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val plugins = repository.getPlugins()
            _state.update { it.copy(items = repository.toCards(plugins)) }
        }
    }

    fun onIntent(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.PluginClicked ->
                navigator.open(Screen.PluginDetail(intent.pluginId))
        }
    }
}
