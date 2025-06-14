package com.gustavo.mercadoautonomo

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}