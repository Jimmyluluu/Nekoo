package com.lu

import com.lu.data.AppDataBase
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.lu.plugins.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        AppDataBase.initDataBase()
        install(CORS) {
            anyHost()
        }
        install(ContentNegotiation){
            gson()
        }
        configureRouting()
    }.start(wait = true)
}
