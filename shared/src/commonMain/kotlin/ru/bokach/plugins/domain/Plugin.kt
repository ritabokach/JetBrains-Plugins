package ru.bokach.plugins.domain

data class Plugin(
    val id: Int,
    val xmlId: String,
    val name: String,
    val preview: String,
    val downloads: Long,
    val rating: Double?,
    val pricingModel: PricingModel,
    val tags: List<String>,
    val vendorId: Int,
    val urls: PluginUrls,
)

enum class PricingModel { FREE, FREEMIUM, PAID }

data class PluginUrls(
    val docUrl: String = "",
    val sourceCodeUrl: String = "",
    val bugtrackerUrl: String = "",
    val forumUrl: String = "",
)
