package ru.bokach.plugins.ui.screens.vendor

import ru.bokach.plugins.ui.model.PluginCardUi
import ru.bokach.plugins.ui.model.VendorUi

sealed interface VendorState {
    data object Loading : VendorState
    data object Error : VendorState

    /** [plugins] is the first page; [totalPlugins] is how many the vendor has in all. */
    data class Content(
        val vendor: VendorUi,
        val plugins: List<PluginCardUi>,
        val totalPlugins: Int,
    ) : VendorState
}
