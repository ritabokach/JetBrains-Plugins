package ru.bokach.plugins.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ru.bokach.plugins.Screen
import ru.bokach.plugins.catalog.CatalogViewModel
import ru.bokach.plugins.detail.PluginDetailViewModel
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.ui.screens.catalog.CatalogScreen
import ru.bokach.plugins.ui.screens.detail.PluginDetailScreen
import ru.bokach.plugins.ui.screens.vendor.VendorScreen
import ru.bokach.plugins.vendor.VendorViewModel

private const val TRANSITION_MS = 300

@Composable
fun AppNavDisplay(
    navigator: Navigator,
    repository: CatalogRepository,
    modifier: Modifier = Modifier,
) {
    val backStack by navigator.backStack.collectAsStateWithLifecycle()
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { navigator.back() },
        transitionSpec = { slide(SlideDirection.Start) },
        popTransitionSpec = { slide(SlideDirection.End) },
        predictivePopTransitionSpec = { slide(SlideDirection.End) },
        entryProvider = entryProvider {
            entry<Screen.Catalog> {
                val viewModel: CatalogViewModel = viewModel {
                    CatalogViewModel(navigator, repository)
                }
                val state by viewModel.state.collectAsStateWithLifecycle()
                CatalogScreen(state = state, onIntent = viewModel::onIntent)
            }

            entry<Screen.PluginDetail> { key ->
                val viewModel: PluginDetailViewModel = viewModel(key = "plugin-${key.pluginId}") {
                    PluginDetailViewModel(key.pluginId, navigator, repository)
                }
                val plugin by viewModel.state.collectAsStateWithLifecycle()
                plugin?.let {
                    PluginDetailScreen(plugin = it, onIntent = viewModel::onIntent)
                }
            }

            entry<Screen.VendorPage> { key ->
                val viewModel: VendorViewModel = viewModel(key = "vendor-${key.vendorId}") {
                    VendorViewModel(key.vendorId, navigator, repository)
                }
                val state by viewModel.state.collectAsStateWithLifecycle()
                VendorScreen(state = state, onIntent = viewModel::onIntent)
            }
        },
    )
}

private fun AnimatedContentTransitionScope<*>.slide(direction: SlideDirection): ContentTransform =
    (slideIntoContainer(direction, tween(TRANSITION_MS)) + fadeIn(tween(TRANSITION_MS)))
        .togetherWith(
            slideOutOfContainer(direction, tween(TRANSITION_MS)) + fadeOut(tween(TRANSITION_MS)),
        )
