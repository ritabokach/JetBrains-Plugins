package ru.bokach.plugins.ui.screens.vendor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.vendor_country
import ru.bokach.plugins.resources.vendor_plugins
import ru.bokach.plugins.resources.vendor_site
import ru.bokach.plugins.resources.vendor_unverified
import ru.bokach.plugins.resources.vendor_verified
import ru.bokach.plugins.ui.components.FactRow
import ru.bokach.plugins.ui.components.PluginCard
import ru.bokach.plugins.ui.model.VendorUi

@Composable
fun VendorScreen(
    state: VendorState,
    onIntent: (VendorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        state.vendor?.let { vendor ->
            item { VendorHeader(vendor) }
            item {
                Text(
                    text = stringResource(Res.string.vendor_plugins, state.plugins.size),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
        items(state.plugins, key = { it.id }) { plugin ->
            PluginCard(
                plugin = plugin,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(VendorIntent.PluginClicked(plugin.id)) },
            )
        }
    }
}

@Composable
private fun VendorHeader(vendor: VendorUi) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(vendor.name, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = stringResource(
                if (vendor.isVerified) Res.string.vendor_verified else Res.string.vendor_unverified,
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        vendor.country?.let { FactRow(stringResource(Res.string.vendor_country), it) }
        vendor.site?.let { FactRow(stringResource(Res.string.vendor_site), it) }
    }
}
