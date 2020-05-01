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
    private var graphqlEndpoint: String? = null
    private val graphqlPathSegment = "/graphql"
    private val contentTypeHeaderKey = "Content-Type"
    private val applicationJsonValue: String = "application/json"
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
        graphqlEndpoint = "${wireMockServer.baseUrl()}$graphqlPathSegment"
        authorizationManager = AuthorizationManager(URI(graphqlEndpoint!!))
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
                post(graphqlPathSegment)
                        .withRequestBody(equalToJson(httpRequestBody))
                        .withHeader(contentTypeHeaderKey, equalTo(applicationJsonValue))
                        .willReturn(aResponse()
                                .withHeader(contentTypeHeaderKey, applicationJsonValue)
                                .withBody(httpResponseBody)
                        )
        )
    }

    private val graphQLOperation = """mutation {
                createSubject(user: 
                        { 
                            externalIdentifier: \"$identifier\"
                            name: \"$name\"
                        }
                ) {
                    identifier
                    externalIdentifier
                    name
                }
            }"""

    private val httpRequestBody = """
            {
                "query": "$graphQLOperation",
                "variables": {}
            }""".toNormalizedGraphQL()

    private val httpResponseBody = """
        {
            "data": {
                "createSubject": {
                    "identifier": "$externalIdentifier",
                    "externalIdentifier": "$identifier",
                    "name": "$name"
                }
            }
        }
    """.minifyJSON()

    private fun String.toNormalizedGraphQL(): String {
        return this
                .trim()
                .trimIndent()
                .replace('\n', ' ')
                .replace(Regex("[ ]+"), " ")
    }

    private fun String.minifyJSON(): String {
        return this
                .trim()
                .trimIndent()
                .filterNot { c: Char -> c.isWhitespace() }
    }
}
