package com.davidgracia.software.authorizationmanager.sdk.kotlin

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class AuthorizationManager(val host: URI) {
    fun getUser(identifier: String): User {
        val httpClient: HttpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build()

        val requestBody = """{"query":"{getUser(identifier: \"$identifier\") {\n    identifier\n  }\n}","variables":{}}"""
        val httpRequest: HttpRequest =
                HttpRequest
                        .newBuilder()
                        .uri(host)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build()

        val response: HttpResponse<String> = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        return User("asdsdaf", "asddfa")
    }

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

/*
request
mutation {
  createUser(user: { name: "Carlos" }) {
    name
    identifier
  }
}

response
{
  "data": {
    "createUser": {
      "name": "Carlos",
      "identifier": "fc586ad9-fe83-4c7e-92b2-c4e44cfbfb04"
    }
  }
}
----------------------------------------------------------
request
query {
  getUser(identifier: "1") {
    identifier
    name
  }
}

response
{
  "data": {
    "getUser": {
      "identifier": "1",
      "name": "John"
    }
  }
}

*/
