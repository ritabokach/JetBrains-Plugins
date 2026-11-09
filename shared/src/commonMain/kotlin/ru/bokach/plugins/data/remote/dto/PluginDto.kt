package ru.bokach.plugins.data.remote.dto

import kotlinx.serialization.Serializable

/** `GET /api/plugins/{id}`. */
@Serializable
internal data class PluginDto(
    val id: Int,
    val xmlId: String = "",
    val name: String,
    val preview: String = "",
    val downloads: Long = 0,
    val pricingModel: String = "",
    val tags: List<TagDto> = emptyList(),
    val vendor: VendorDto? = null,
    val urls: PluginUrlsDto = PluginUrlsDto(),
)

@Serializable
internal data class TagDto(
    val name: String,
)

@Serializable
internal data class PluginUrlsDto(
    val docUrl: String = "",
    val sourceCodeUrl: String = "",
    val bugtrackerUrl: String = "",
    val forumUrl: String = "",
)

/**
 * `GET /api/plugins/{id}/rating`. For a plugin nobody voted for the API still
 * returns a site-wide prior as `meanRating`, so `votes` decides whether the
 * rating is real.
 */
@Serializable
internal data class RatingDto(
    val votes: Map<String, Int> = emptyMap(),
    val meanRating: Double? = null,
)
