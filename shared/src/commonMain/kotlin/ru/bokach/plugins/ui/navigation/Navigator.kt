package ru.bokach.plugins.ui.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.bokach.plugins.Screen

class Navigator {

    private val _backStack = MutableStateFlow<List<Screen>>(listOf(Screen.Catalog))
    val backStack = _backStack.asStateFlow()

    fun open(screen: Screen) {
        _backStack.update { it + screen }
    }

    fun back() {
        _backStack.update { stack -> if (stack.size > 1) stack.dropLast(1) else stack }
    }
}
