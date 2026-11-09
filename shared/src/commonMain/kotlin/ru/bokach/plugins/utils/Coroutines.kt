package ru.bokach.plugins.utils

import kotlinx.coroutines.CancellationException

/**
 * [runCatching] for suspend code. Cancellation is not a failure: it is
 * rethrown, so a request cancelled by a newer one never shows up as an error.
 */
internal suspend inline fun <T> suspendRunCatching(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
