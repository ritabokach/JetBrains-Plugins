package ru.bokach.plugins.data.remote

import ru.bokach.plugins.data.remote.dto.PluginDto
import ru.bokach.plugins.data.remote.dto.PluginSummaryDto
import ru.bokach.plugins.data.remote.dto.RatingDto
import ru.bokach.plugins.data.remote.dto.SearchResponseDto
import ru.bokach.plugins.data.remote.dto.VendorDto
import ru.bokach.plugins.domain.Plugin
import ru.bokach.plugins.domain.PluginPage
import ru.bokach.plugins.domain.PluginSummary
import ru.bokach.plugins.domain.PluginUrls
import ru.bokach.plugins.domain.PricingModel
import ru.bokach.plugins.domain.Vendor

internal fun SearchResponseDto.toDomain(): PluginPage =
    PluginPage(items = plugins.map { it.toDomain() }, total = total)

internal fun PluginSummaryDto.toDomain(): PluginSummary = PluginSummary(
    id = id,
    name = name,
    downloads = downloads,
    rating = rating,
    pricingModel = pricingModelOf(pricingModel),
    tags = tags,
    vendorName = vendor?.name.orEmpty(),
)

internal fun PluginDto.toDomain(rating: RatingDto): Plugin = Plugin(
    id = id,
    xmlId = xmlId,
    name = name,
    preview = preview,
    downloads = downloads,
    rating = rating.meanRating.takeIf { rating.votes.isNotEmpty() },
    pricingModel = pricingModelOf(pricingModel),
    tags = tags.map { it.name },
    vendorSlug = vendor?.name,
    vendorName = vendor?.displayName.orEmpty(),
    urls = PluginUrls(
        docUrl = urls.docUrl,
        sourceCodeUrl = urls.sourceCodeUrl,
        bugtrackerUrl = urls.bugtrackerUrl,
        forumUrl = urls.forumUrl,
    ),
)

internal fun VendorDto.toDomain(): Vendor = Vendor(
    slug = name,
    name = displayName,
    url = url,
    country = country,
    isVerified = isVerified,
)

private val VendorDto.displayName: String
    get() = publicName?.takeIf { it.isNotBlank() } ?: name

/** A pricing model this app does not know yet is shown as paid rather than as free. */
private fun pricingModelOf(value: String): PricingModel =
    PricingModel.entries.firstOrNull { it.name == value } ?: PricingModel.PAID
