package com.suncar.solarsurvivor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform