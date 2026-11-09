package ru.bokach.plugins.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bokach.plugins.Screen
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.model.LoadStatus
import ru.bokach.plugins.ui.model.PluginCardUi
import ru.bokach.plugins.ui.model.toCards
import ru.bokach.plugins.ui.navigation.Navigator
import ru.bokach.plugins.utils.suspendRunCatching

private const val SEARCH_DEBOUNCE_MS = 400L

@OptIn(FlowPreview::class)
class CatalogViewModel(
    private val navigator: Navigator,
    private val repository: CatalogRepository,
    private val computationDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state.asStateFlow()

    private val query = MutableStateFlow("")

    /** The query the loaded pages belong to. */
    private var activeQuery = ""

    /** How many rows the server has returned for [activeQuery]: the next page's offset. */
    private var fetchedCount = 0

    /** The only request in flight. A new query cancels it, whatever it was loading. */
    private var loadJob: Job? = null

    init {
        // Typing is debounced so that every keystroke is not a request; clearing
        // the field brings the full list back at once.
        query
            .map { it.trim() }
            .debounce { if (it.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
            .distinctUntilChanged()
            .onEach { loadFirstPage(it) }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.query) }
                query.value = intent.query
            }
            is CatalogIntent.PluginClicked ->
                navigator.open(Screen.PluginDetail(intent.pluginId))
            CatalogIntent.LoadMore -> {
                if (_state.value.appendStatus == LoadStatus.Idle) loadNextPage()
            }
            CatalogIntent.Retry -> when {
                _state.value.status == LoadStatus.Error -> loadFirstPage(activeQuery)
                _state.value.appendStatus == LoadStatus.Error -> loadNextPage()
            }
        }
    }

    private fun loadFirstPage(query: String) {
        loadJob?.cancel()
        activeQuery = query
        fetchedCount = 0
        _state.update {
            it.copy(
                items = emptyList(),
                status = LoadStatus.Loading,
                appendStatus = LoadStatus.Idle,
                endReached = false,
            )
        }
        loadJob = viewModelScope.launch {
            suspendRunCatching { fetchPage(query, loaded = emptyList()) }
                .onSuccess { page ->
                    fetchedCount = page.fetchedCount
                    _state.update {
                        it.copy(items = page.items, status = LoadStatus.Idle, endReached = page.endReached)
                    }
                }
                .onFailure { _state.update { it.copy(status = LoadStatus.Error) } }
        }
    }

    private fun loadNextPage() {
        val current = _state.value
        if (loadJob?.isActive == true || current.status != LoadStatus.Idle || current.endReached) return
        val query = activeQuery
        _state.update { it.copy(appendStatus = LoadStatus.Loading) }
        loadJob = viewModelScope.launch {
            suspendRunCatching { fetchPage(query, loaded = current.items) }
                .onSuccess { page ->
                    fetchedCount = page.fetchedCount
                    _state.update {
                        it.copy(items = page.items, appendStatus = LoadStatus.Idle, endReached = page.endReached)
                    }
                }
                .onFailure { _state.update { it.copy(appendStatus = LoadStatus.Error) } }
        }
    }

    /**
     * Requests the page after [fetchedCount] and glues it to [loaded] off the
     * main thread. The marketplace can shift a plugin between pages while we
     * scroll, so duplicates are dropped: the list keys items by id.
     */
    private suspend fun fetchPage(query: String, loaded: List<PluginCardUi>): LoadedPage {
        val offset = fetchedCount
        val page = repository.searchPlugins(query, offset)
        return withContext(computationDispatcher) {
            val fetched = offset + page.items.size
            LoadedPage(
                items = (loaded + page.items.toCards()).distinctBy { it.id },
                fetchedCount = fetched,
                endReached = page.items.isEmpty() || fetched >= page.total,
            )
        }
    }

    private class LoadedPage(
        val items: List<PluginCardUi>,
        val fetchedCount: Int,
        val endReached: Boolean,
    )
}
