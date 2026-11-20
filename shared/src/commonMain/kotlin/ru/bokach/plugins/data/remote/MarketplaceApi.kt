package ru.bokach.plugins.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.bokach.plugins.data.remote.dto.PluginDto
import ru.bokach.plugins.data.remote.dto.RatingDto
import ru.bokach.plugins.data.remote.dto.SearchResponseDto
import ru.bokach.plugins.data.remote.dto.VendorDto

/**
 * Thin wrapper over the JetBrains Marketplace REST API. Returns raw DTOs;
 * turning them into domain models is the repository's job.
 */
internal class MarketplaceApi(
    private val client: HttpClient,
    private val baseUrl: String = ApiConfig.BASE_URL,
    private val parsingDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    /** Results come most downloaded first; without `orderBy` the API uses its own mixed order. */
    suspend fun searchPlugins(
        query: String,
        offset: Int,
        max: Int,
        organization: String? = null,
    ): SearchResponseDto = get("api", "searchPlugins") {
        parameter("max", max)
        parameter("offset", offset)
        parameter("orderBy", "downloads")
        if (query.isNotBlank()) parameter("search", query)
        if (organization != null) parameter("organization", organization)
    }

    suspend fun plugin(id: Int): PluginDto = get("api", "plugins", id.toString())

    suspend fun rating(pluginId: Int): RatingDto =
        get("api", "plugins", pluginId.toString(), "rating")

    suspend fun vendor(name: String): VendorDto = get("api", "vendors", name)

    /**
     * Reads the body as text and decodes it off the caller's dispatcher: the
     * marketplace answers with `application/graphql+json`, and a large page
     * should not be parsed on the UI thread.
     */
    private suspend inline fun <reified T> get(
        vararg path: String,
        crossinline block: HttpRequestBuilder.() -> Unit = {},
    ): T {
        val body = client.get(baseUrl) {
            url { appendPathSegments(*path) }
            block()
        }.bodyAsText()
        return withContext(parsingDispatcher) { json.decodeFromString<T>(body) }
    }

    private companion object {
        /** Unknown fields are skipped, so a new field in the API does not break us. */
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
}
