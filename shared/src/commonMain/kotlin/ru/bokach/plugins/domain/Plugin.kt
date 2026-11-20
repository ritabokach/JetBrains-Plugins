package ru.bokach.plugins.domain

/** A plugin as the search returns it: enough for a card in a list. */
data class PluginSummary(
    val id: Int,
    val name: String,
    val downloads: Long,
    val rating: Double?,
    val pricingModel: PricingModel,
    val tags: List<String>,
    val vendorName: String,
)

/** One page of search results and how many matches there are in total. */
data class PluginPage(
    val items: List<PluginSummary>,
    val total: Int,
)

/** The full plugin page. */
data class Plugin(
    val id: Int,
    val xmlId: String,
    val name: String,
    val preview: String,
    val downloads: Long,
    val rating: Double?,
    val pricingModel: PricingModel,
    val tags: List<String>,
    val vendorSlug: String?,
    val vendorName: String,
    val urls: PluginUrls,
)

enum class PricingModel { FREE, FREEMIUM, PAID }

data class PluginUrls(
    val docUrl: String = "",
    val sourceCodeUrl: String = "",
    val bugtrackerUrl: String = "",
    val forumUrl: String = "",
)
