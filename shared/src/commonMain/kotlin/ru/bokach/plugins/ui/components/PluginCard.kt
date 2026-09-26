package ru.bokach.plugins.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.card_downloads
import ru.bokach.plugins.resources.card_no_rating
import ru.bokach.plugins.resources.card_rating
import ru.bokach.plugins.ui.model.PluginCardUi

@Composable
fun PluginCard(plugin: PluginCardUi, modifier: Modifier = Modifier) {
    CardSurface(modifier) {
        Row {
            PluginAvatar(plugin.name)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = plugin.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = plugin.vendorName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.card_downloads, plugin.downloads),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = plugin.rating
                            ?.let { stringResource(Res.string.card_rating, it) }
                            ?: stringResource(Res.string.card_no_rating),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (plugin.tags.isNotEmpty()) {
                    TagRow(plugin.tags, Modifier.padding(top = 8.dp).fillMaxWidth())
                }
            }
            Spacer(Modifier.width(8.dp))
            PricingChip(plugin.pricing)
        }
    }
}
