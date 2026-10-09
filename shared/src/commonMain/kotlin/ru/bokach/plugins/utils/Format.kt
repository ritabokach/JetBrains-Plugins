package ru.bokach.plugins.utils

import kotlin.math.roundToInt

internal fun formatDownloads(downloads: Long): String = when {
    downloads >= 1_000_000 -> "${round1(downloads / 1_000_000.0)}M"
    downloads >= 1_000 -> "${round1(downloads / 1_000.0)}K"
    else -> downloads.toString()
}

internal fun formatRating(rating: Double): String = ((rating * 100).roundToInt() / 100.0).toString()

private fun round1(value: Double): String = ((value * 10).roundToInt() / 10.0).toString()
