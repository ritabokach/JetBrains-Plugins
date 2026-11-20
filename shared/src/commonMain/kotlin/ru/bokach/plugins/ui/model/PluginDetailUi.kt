package ru.bokach.plugins.ui.model

import ru.bokach.plugins.domain.Plugin
import ru.bokach.plugins.domain.PricingModel
import ru.bokach.plugins.utils.formatDownloads
import ru.bokach.plugins.utils.formatRating

data class PluginDetailUi(
    val id: Int,
    val name: String,
    val xmlId: String,
    val preview: String,
    val downloads: String,
    val rating: String?,
    val pricing: PricingModel,
    val tags: List<String>,
    val vendorSlug: String?,
    val vendorName: String,
    val links: List<PluginLink>,
)

data class PluginLink(val kind: PluginLinkKind, val url: String)

enum class PluginLinkKind { DOC, SOURCE, BUGTRACKER, FORUM }

internal fun Plugin.toDetail(): PluginDetailUi = PluginDetailUi(
    id = id,
    name = name,
    xmlId = xmlId,
    preview = preview,
    downloads = formatDownloads(downloads),
    rating = rating?.let(::formatRating),
    pricing = pricingModel,
    tags = tags,
    vendorSlug = vendorSlug,
    vendorName = vendorName,
    links = links(),
)

private fun Plugin.links(): List<PluginLink> = listOfNotNull(
    link(PluginLinkKind.DOC, urls.docUrl),
    link(PluginLinkKind.SOURCE, urls.sourceCodeUrl),
    link(PluginLinkKind.BUGTRACKER, urls.bugtrackerUrl),
    link(PluginLinkKind.FORUM, urls.forumUrl),
)

private fun link(kind: PluginLinkKind, url: String): PluginLink? =
    url.takeIf { it.isNotBlank() }?.let { PluginLink(kind, it) }
