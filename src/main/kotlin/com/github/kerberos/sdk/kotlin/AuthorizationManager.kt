package com.github.kerberos.sdk.kotlin

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class AuthorizationManager(val host: URI) {

    fun create(subjectData: CreateSubjectData): Subject {
        val httpClient: HttpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build()

        val graphQLOperation = """mutation {
                        createSubject(subject: 
                                { 
                                    externalIdentifier: \"${subjectData.identifier}\"
                                    name: \"${subjectData.name}\"
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
        val createdSubjectJson: JsonObject = jsonObject.getAsJsonObject("data").getAsJsonObject("createSubject")

        return Subject(
                identifier = createdSubjectJson.get("externalIdentifier").asString,
                externalIdentifier = createdSubjectJson.get("identifier").asString,
                name = createdSubjectJson.get("name").asString
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
