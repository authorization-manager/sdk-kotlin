package com.davidgracia.software.authorizationmanager.sdk.kotlin

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class AuthorizationManager(val host: URI) {

    fun create(userData: CreateUserData): User {
        val httpClient: HttpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build()

        val graphQLOperation = """mutation {
                        createUser(user: 
                                { 
                                    externalIdentifier: \"${userData.identifier}\"
                                    name: \"${userData.name}\"
                                }
                        ) {
                            identifier
                            externalIdentifier
                            name
                        }
                    }"""

        val httpRequestBody: String = """
            {
                "query": "$graphQLOperation",
                "variables": {}
            }""".toNormalizedGraphQL()

        val httpRequest: HttpRequest =
                HttpRequest
                        .newBuilder()
                        .uri(host)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(httpRequestBody))
                        .build()

        val response: HttpResponse<String> = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        val jsonElement: JsonElement = JsonParser.parseString(response.body())
        val jsonObject: JsonObject = jsonElement.asJsonObject
        val createdUserJson: JsonObject = jsonObject.getAsJsonObject("data").getAsJsonObject("createUser")

        return User(
                identifier = createdUserJson.get("externalIdentifier").asString,
                externalIdentifier = createdUserJson.get("identifier").asString,
                name = createdUserJson.get("name").asString
        )
    }
}

private fun String.toNormalizedGraphQL(): String {
    return this
            .trim()
            .trimIndent()
            .replace('\n', ' ')
            .replace(Regex("[ ]+"), " ")
}
