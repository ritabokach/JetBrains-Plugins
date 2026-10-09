package ru.bokach.plugins.ui.model

import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.domain.Plugin
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

internal suspend fun CatalogRepository.toCards(plugins: List<Plugin>): List<PluginCardUi> =
    plugins.map { plugin ->
        PluginCardUi(
            id = plugin.id,
            name = plugin.name,
            vendorName = getVendor(plugin.vendorId).name,
            downloads = formatDownloads(plugin.downloads),
            rating = plugin.rating?.let(::formatRating),
            pricing = plugin.pricingModel,
            tags = plugin.tags,
        )
    }
