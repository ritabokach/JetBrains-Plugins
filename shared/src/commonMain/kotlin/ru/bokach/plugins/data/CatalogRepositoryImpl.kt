package ru.bokach.plugins.data

import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.domain.Plugin
import ru.bokach.plugins.domain.Vendor

class CatalogRepositoryImpl : CatalogRepository {

    override suspend fun searchPlugins(query: String): List<Plugin> {
        val needle = query.trim()
        if (needle.isEmpty()) return mockPlugins
        return mockPlugins.filter { plugin ->
            plugin.name.contains(needle, ignoreCase = true) ||
                plugin.tags.any { it.contains(needle, ignoreCase = true) }
        }
    }

    override suspend fun getPlugin(id: Int): Plugin = mockPlugins.first { it.id == id }

    override suspend fun getVendor(id: Int): Vendor = mockVendors.first { it.id == id }

    override suspend fun getPluginsByVendor(vendorId: Int): List<Plugin> =
        mockPlugins.filter { it.vendorId == vendorId }
}
