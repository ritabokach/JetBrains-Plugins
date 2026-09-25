package ru.bokach.plugins

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform