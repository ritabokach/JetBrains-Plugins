package ru.bokach.plugins.domain

interface CatalogRepository {

    /** A page of plugins starting at [offset]. An empty [query] lists the whole marketplace. */
    suspend fun searchPlugins(query: String, offset: Int): PluginPage

    suspend fun getPlugin(id: Int): Plugin

    suspend fun getVendor(slug: String): Vendor

    suspend fun getPluginsByVendor(vendorName: String): PluginPage
}
