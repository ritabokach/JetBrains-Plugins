package ru.bokach.plugins.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout

/**
 * Builds a client on the platform's own engine: OkHttp on Android, CIO on the
 * JVM, the browser's fetch on web, URLSession on iOS. Common code only adds
 * the shared configuration on top.
 */
internal expect fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

internal fun createHttpClient(): HttpClient = platformHttpClient {
    expectSuccess = true
    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 10_000
    }
}
