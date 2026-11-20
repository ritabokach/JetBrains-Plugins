package ru.bokach.plugins.ui.screens.vendor

import ru.bokach.plugins.ui.model.PluginCardUi
import ru.bokach.plugins.ui.model.VendorUi

data class VendorState(
    val vendor: VendorUi? = null,
    val plugins: List<PluginCardUi> = emptyList(),
)
