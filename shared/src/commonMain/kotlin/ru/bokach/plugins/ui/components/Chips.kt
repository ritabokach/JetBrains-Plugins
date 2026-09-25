package ru.bokach.plugins.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.bokach.plugins.domain.PricingModel
import ru.bokach.plugins.resources.Res
import ru.bokach.plugins.resources.pricing_free
import ru.bokach.plugins.resources.pricing_freemium
import ru.bokach.plugins.resources.pricing_paid
import ru.bokach.plugins.ui.pricingColor

@Composable
fun PricingChip(pricing: PricingModel, modifier: Modifier = Modifier) {
    val label = when (pricing) {
        PricingModel.FREE -> Res.string.pricing_free
        PricingModel.FREEMIUM -> Res.string.pricing_freemium
        PricingModel.PAID -> Res.string.pricing_paid
    }
    Chip(
        text = stringResource(label),
        container = pricingColor(pricing),
        content = Color.White,
        modifier = modifier,
    )
}

@Composable
fun TagChip(tag: String, modifier: Modifier = Modifier) {
    Chip(
        text = tag,
        container = MaterialTheme.colorScheme.surfaceVariant,
        content = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagRow(tags: List<String>, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        tags.forEach { TagChip(it) }
    }
}

@Composable
private fun Chip(text: String, container: Color, content: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = container,
        contentColor = content,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}
