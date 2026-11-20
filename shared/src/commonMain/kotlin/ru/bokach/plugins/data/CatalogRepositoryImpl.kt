package ru.bokach.plugins.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.bokach.plugins.data.remote.MarketplaceApi
import ru.bokach.plugins.data.remote.toDomain
import ru.bokach.plugins.domain.CatalogRepository
import ru.bokach.plugins.domain.Plugin
import ru.bokach.plugins.domain.PluginPage
import ru.bokach.plugins.domain.Vendor

internal class CatalogRepositoryImpl(
    private val api: MarketplaceApi,
) : CatalogRepository {

    override suspend fun searchPlugins(query: String, offset: Int): PluginPage =
        api.searchPlugins(query = query.trim(), offset = offset, max = PAGE_SIZE).toDomain()

    /** The detail and the rating are separate endpoints, so both are requested at once. */
    override suspend fun getPlugin(id: Int): Plugin = coroutineScope {
        val plugin = async { api.plugin(id) }
        val rating = async { api.rating(id) }
        plugin.await().toDomain(rating.await())
    }

    override suspend fun getVendor(slug: String): Vendor = api.vendor(slug).toDomain()

    /**
     * The `organization` filter matches loosely ("zhihao" finds "Zhihao Zhang"),
     * so only the vendor's own plugins are kept.
     */
    override suspend fun getPluginsByVendor(vendorName: String): PluginPage {
        val page = api.searchPlugins(
            query = "",
            offset = 0,
            max = PAGE_SIZE,
            organization = vendorName,
        ).toDomain()
        return page.copy(items = page.items.filter { it.vendorName == vendorName })
    }

    companion object {
        /** The largest page `/api/searchPlugins` agrees to return. */
        const val PAGE_SIZE = 20
    }
}
