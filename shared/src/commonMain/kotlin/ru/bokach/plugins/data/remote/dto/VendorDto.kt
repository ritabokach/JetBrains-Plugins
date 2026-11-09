package ru.bokach.plugins.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * `GET /api/vendors/{name}`, also embedded in the plugin detail. `name` is the
 * vendor's address in the API, `publicName` is what people see.
 */
@Serializable
internal data class VendorDto(
    val name: String,
    val publicName: String? = null,
    val url: String = "",
    val country: String? = null,
    val isVerified: Boolean = false,
)
