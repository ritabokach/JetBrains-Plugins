package ru.bokach.plugins.domain

/**
 * [slug] is how the API addresses the vendor, [name] is the public name shown
 * to people and used in search results.
 */
data class Vendor(
    val slug: String,
    val name: String,
    val url: String,
    val country: String?,
    val isVerified: Boolean,
)
