package com.example.socketsFacade.internal

import com.example.socketsFacade.HttpBroadcastMessage
import com.example.socketsFacade.HttpBroadcastResponseElement
import com.example.socketsFacade.IHttpDiscoveryClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

internal class HttpDiscoveryClient : IHttpDiscoveryClient {

    override suspend fun sendMessage(message: HttpBroadcastMessage): List<HttpBroadcastResponseElement>? {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                    },
                )
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
        }

        return runCatching {
            client.post("https://natpunch.salmonpond-b9439f7b.germanywestcentral.azurecontainerapps.io") {
                setBody(message)
            }.body<String?>()?.let {
                Json.decodeFromString<List<HttpBroadcastResponseElement>>(it)
            }
        }.onFailure {
            coroutineContext.ensureActive()
            it.printStackTrace()
        }.getOrNull()
    }
}
