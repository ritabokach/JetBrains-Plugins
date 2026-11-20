package ru.bokach.plugins.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

internal actual fun platformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(CIO, config)
