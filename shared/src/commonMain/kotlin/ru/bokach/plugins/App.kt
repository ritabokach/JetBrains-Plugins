package ru.bokach.plugins

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.data.CatalogRepositoryImpl
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.action_toggle_theme
import ru.bokach.plugins.resources.ic_theme
import ru.bokach.plugins.ui.AppTheme
import ru.bokach.plugins.ui.components.AppScaffold
import ru.bokach.plugins.ui.navigation.AppNavDisplay
import ru.bokach.plugins.ui.navigation.Navigator

@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }

    AppTheme(darkTheme) {
        val repository: CatalogRepository = remember { CatalogRepositoryImpl() }
        val navigator = remember { Navigator() }

        val backStack by navigator.backStack.collectAsStateWithLifecycle()
        val canGoBack by remember { derivedStateOf { backStack.size > 1 } }

        AppScaffold(
            onBack = navigator::back.takeIf { canGoBack },
            actions = {
                IconButton(onClick = { darkTheme = !darkTheme }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_theme),
                        contentDescription = stringResource(Res.string.action_toggle_theme),
                    )
                }
            },
        ) { modifier ->
            AppNavDisplay(
                navigator = navigator,
                repository = repository,
                modifier = modifier,
            )
        }
    }
}
