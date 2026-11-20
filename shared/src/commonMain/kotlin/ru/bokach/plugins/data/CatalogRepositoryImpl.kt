package ru.bokach.plugins.data

import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.domain.Plugin
import ru.bokach.plugins.domain.Vendor

class CatalogRepositoryImpl : CatalogRepository {

    override suspend fun getPlugins(): List<Plugin> = mockPlugins

    override suspend fun getPlugin(id: Int): Plugin = mockPlugins.first { it.id == id }

    override suspend fun getVendor(id: Int): Vendor = mockVendors.first { it.id == id }

    override suspend fun getPluginsByVendor(vendorId: Int): List<Plugin> =
        mockPlugins.filter { it.vendorId == vendorId }
}
