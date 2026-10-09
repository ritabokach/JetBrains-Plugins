package ru.bokach.plugins.ui.model

import ru.bokach.plugins.domain.Vendor

data class VendorUi(
    val id: Int,
    val name: String,
    val site: String?,
    val country: String?,
    val isVerified: Boolean,
)

internal fun Vendor.toUi(): VendorUi = VendorUi(
    id = id,
    name = name,
    site = url.takeIf { it.isNotBlank() }?.let(::hostOf),
    country = country,
    isVerified = isVerified,
)

private fun hostOf(url: String): String = url
    .substringAfter("://")
    .removePrefix("www.")
    .trimEnd('/')
    .substringBefore('/')
