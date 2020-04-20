package com.davidgracia.software.authorizationmanager.sdk.kotlin

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class AuthorizationManager(val host: URI) {
    fun save(user: User): User {
        val httpClient: HttpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build()

        val httpRequest: HttpRequest =
                HttpRequest
                        .newBuilder()
                        .uri(host)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(user.toJson()))
                        .build()

        val response: HttpResponse<String> = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        return response.body().toUser()
    }

    private fun String.toUser(): User {
        return Gson().fromJson(this, User::class.java)
    }

    private fun User.toJson(): String {
        return Gson().toJson(this)
    }
}
