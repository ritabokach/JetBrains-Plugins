package ru.bokach.plugins.domain

interface CatalogRepository {

    suspend fun searchPlugins(query: String): List<Plugin>

    suspend fun getPlugin(id: Int): Plugin

    suspend fun getVendor(id: Int): Vendor

    suspend fun getPluginsByVendor(vendorId: Int): List<Plugin>
}
