package ru.bokach.plugins.domain

data class Vendor(
    val id: Int,
    val name: String,
    val url: String,
    val country: String?,
    val isVerified: Boolean,
)
