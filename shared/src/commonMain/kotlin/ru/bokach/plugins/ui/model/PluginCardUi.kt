package ru.bokach.plugins.ui.model

import ru.bokach.plugins.domain.PluginSummary
import ru.bokach.plugins.domain.PricingModel
import ru.bokach.plugins.utils.formatDownloads
import ru.bokach.plugins.utils.formatRating

data class PluginCardUi(
    val id: Int,
    val name: String,
    val vendorName: String,
    val downloads: String,
    val rating: String?,
    val pricing: PricingModel,
    val tags: List<String>,
)

internal fun List<PluginSummary>.toCards(): List<PluginCardUi> =
    map { plugin ->
        PluginCardUi(
            id = plugin.id,
            name = plugin.name,
            vendorName = plugin.vendorName,
            downloads = formatDownloads(plugin.downloads),
            rating = plugin.rating?.let(::formatRating),
            pricing = plugin.pricingModel,
            tags = plugin.tags,
        )
    }
