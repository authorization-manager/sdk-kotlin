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

        val subjectPostRequestDocument = """
        {
          "data": {
            "type": "subjects",
            "attributes": {
                "externalId": "${subjectData.identifier}",
                "name": "${subjectData.name}"
            }
          }
        }
        """

        val httpRequest: HttpRequest =
                HttpRequest
                        .newBuilder()
                        .uri(host)
                        .header("Content-Type", "application/vnd.api+json")
                        .POST(HttpRequest.BodyPublishers.ofString(subjectPostRequestDocument))
                        .build()

        val response: HttpResponse<String> = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        val jsonElement: JsonElement = JsonParser.parseString(response.body())
        val subjectPostResponseDocument: JsonObject = jsonElement.asJsonObject
        val subjectPostResponseResource: JsonObject = subjectPostResponseDocument.getAsJsonObject("data")
        val subjectPostResponseAttributes: JsonObject = subjectPostResponseResource.getAsJsonObject("attributes")

        return Subject(
                identifier = subjectPostResponseAttributes.get("externalId").asString,
                externalId = subjectPostResponseResource.get("id").asString,
                name = subjectPostResponseAttributes.get("name").asString
        )
    }
}
