package ru.bokach.plugins.data.remote.dto

import kotlinx.serialization.Serializable

/** `GET /api/searchPlugins`: one page of results plus the total match count. */
@Serializable
internal data class SearchResponseDto(
    val plugins: List<PluginSummaryDto> = emptyList(),
    val total: Int = 0,
)

@Serializable
internal data class PluginSummaryDto(
    val id: Int,
    val name: String,
    val downloads: Long = 0,
    val pricingModel: String = "",
    val rating: Double? = null,
    val tags: List<String> = emptyList(),
    val vendor: SearchVendorDto? = null,
)

/** Search results carry only the vendor's public name, not its id. */
@Serializable
internal data class SearchVendorDto(
    val name: String = "",
)
