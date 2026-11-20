package ru.bokach.plugins.ui

import androidx.compose.ui.graphics.Color
import ru.bokach.plugins.domain.PricingModel

fun pricingColor(pricing: PricingModel): Color = when (pricing) {
    PricingModel.FREE -> Color(0xFF2E7D32)
    PricingModel.FREEMIUM -> Color(0xFFA35A00)
    PricingModel.PAID -> Color(0xFF6A3AB2)
}
