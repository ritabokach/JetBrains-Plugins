package ru.bokach.plugins.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bokach.plugins.Screen
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.model.PluginCardUi
import ru.bokach.plugins.ui.model.toCards
import ru.bokach.plugins.ui.navigation.Navigator

class CatalogViewModel(
    private val navigator: Navigator,
    private val repository: CatalogRepository,
) : ViewModel() {

    private val allItems = MutableStateFlow<List<PluginCardUi>>(emptyList())
    private val query = MutableStateFlow("")

    val state: StateFlow<CatalogState> = combine(allItems, query) { items, query ->
        CatalogState(query = query, items = items.filterByName(query))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, CatalogState())

    init {
        viewModelScope.launch {
            val plugins = repository.getPlugins()
            allItems.value = repository.toCards(plugins)
        }
    }

    fun onIntent(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.QueryChanged -> query.update { intent.query }
            is CatalogIntent.PluginClicked ->
                navigator.open(Screen.PluginDetail(intent.pluginId))
        }
    }

    private fun List<PluginCardUi>.filterByName(query: String): List<PluginCardUi> {
        val needle = query.trim()
        if (needle.isEmpty()) return this
        return filter { it.name.contains(needle, ignoreCase = true) }
    }
}
