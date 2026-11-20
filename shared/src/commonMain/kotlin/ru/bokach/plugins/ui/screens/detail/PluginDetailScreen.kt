package ru.bokach.plugins.ui.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.detail_downloads
import ru.bokach.plugins.resources.detail_links
import ru.bokach.plugins.resources.detail_open_vendor
import ru.bokach.plugins.resources.detail_rating
import ru.bokach.plugins.resources.detail_rating_none
import ru.bokach.plugins.resources.detail_tags
import ru.bokach.plugins.resources.detail_vendor
import ru.bokach.plugins.resources.detail_xml_id
import ru.bokach.plugins.resources.ic_chevron_right
import ru.bokach.plugins.resources.link_bugtracker
import ru.bokach.plugins.resources.link_doc
import ru.bokach.plugins.resources.link_forum
import ru.bokach.plugins.resources.link_source
import ru.bokach.plugins.ui.components.CardSurface
import ru.bokach.plugins.ui.components.FactRow
import ru.bokach.plugins.ui.components.PluginAvatar
import ru.bokach.plugins.ui.components.PricingChip
import ru.bokach.plugins.ui.components.Section
import ru.bokach.plugins.ui.components.TagRow
import ru.bokach.plugins.ui.model.PluginDetailUi
import ru.bokach.plugins.ui.model.PluginLinkKind

@Composable
fun PluginDetailScreen(
    plugin: PluginDetailUi,
    onIntent: (PluginDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PluginAvatar(plugin.name, size = 56)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(plugin.name, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = plugin.vendorName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            PricingChip(plugin.pricing)
        }

        Text(plugin.preview, style = MaterialTheme.typography.bodyMedium)

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            FactRow(stringResource(Res.string.detail_xml_id), plugin.xmlId)
            FactRow(stringResource(Res.string.detail_downloads), plugin.downloads)
            FactRow(
                label = stringResource(Res.string.detail_rating),
                value = plugin.rating ?: stringResource(Res.string.detail_rating_none),
            )
        }

        if (plugin.tags.isNotEmpty()) {
            Section(stringResource(Res.string.detail_tags)) {
                TagRow(plugin.tags, Modifier.fillMaxWidth())
            }
        }

        Section(stringResource(Res.string.detail_vendor)) {
            CardSurface(
                Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(PluginDetailIntent.VendorClicked) },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(plugin.vendorName, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = stringResource(Res.string.detail_open_vendor),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(
                        painter = painterResource(Res.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        if (plugin.links.isNotEmpty()) {
            Section(stringResource(Res.string.detail_links)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    plugin.links.forEach { link ->
                        FactRow(stringResource(linkLabel(link.kind)), link.url)
                    }
                }
            }
        }
    }
}

private fun linkLabel(kind: PluginLinkKind) = when (kind) {
    PluginLinkKind.DOC -> Res.string.link_doc
    PluginLinkKind.SOURCE -> Res.string.link_source
    PluginLinkKind.BUGTRACKER -> Res.string.link_bugtracker
    PluginLinkKind.FORUM -> Res.string.link_forum
}
