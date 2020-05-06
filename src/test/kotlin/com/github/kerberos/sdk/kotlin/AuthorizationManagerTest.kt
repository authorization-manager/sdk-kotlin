package com.github.kerberos.sdk.kotlin

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import java.net.URI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AuthorizationManagerTest {

    private val name: String = "Jack"
    private val identifier: String = "1234"
    private val externalIdentifier: String = "xyz"
    private val createSubjectData = CreateSubjectData(identifier = identifier, name = name)
    private val expectedSubject = Subject(identifier = identifier, externalIdentifier = externalIdentifier, name = name)

    private val wireMockServer: WireMockServer
    private var authorizationManager: AuthorizationManager? = null
    private var endpoint: String? = null
    private val path = "/subjects"
    private val contentTypeHeaderKey = "Content-Type"
    private val applicationJsonApiValue: String = "application/vnd.api+json"
    private val externalIdentifierFieldName: String = "externalIdentifier"

    init {
        val wireMockConfiguration: WireMockConfiguration = options()
        with(wireMockConfiguration) {
            bindAddress("localhost")
            dynamicPort()
        }
        wireMockServer = WireMockServer(wireMockConfiguration)
    }

    @BeforeEach
    fun setUp() {
        wireMockServer.start()
        endpoint = "${wireMockServer.baseUrl()}$path"
        authorizationManager = AuthorizationManager(URI(endpoint!!))
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `creates a subject and responds the created subject`() {
        subjectIsCreated()

        authorizationManager!!.create(createSubjectData).let { createdSubject: Subject ->
            assertThat(createdSubject)
                    .usingRecursiveComparison()
                    .ignoringFields(externalIdentifierFieldName)
                    .isEqualTo(expectedSubject)
        }
    }

    private fun subjectIsCreated() {
        wireMockServer.stubFor(
                post(path)
                        .withRequestBody(equalToJson(httpRequestBody))
                        .withHeader(contentTypeHeaderKey, equalTo(applicationJsonApiValue))
                        .willReturn(aResponse()
                                .withHeader(contentTypeHeaderKey, applicationJsonApiValue)
                                .withBody(httpResponseBody)
                        )
        )
    }

    private val httpRequestBody = """
        {
          "data": {
            "type": "subjects",
            "attributes": {
                "externalIdentifier": "$identifier",
                "name": "$name"
            }
          }
        }
        """

    private val httpResponseBody = """
        {
          "data": {
            "id": "$externalIdentifier",
            "type": "subjects",
            "attributes": {
                "externalIdentifier": "$identifier",
                "name": "$name"
            }
          }
        }
        """
}
