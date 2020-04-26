package com.davidgracia.software.authorizationmanager.sdk.kotlin

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI


class AuthorizationManagerTest {

    private val identifier = "1234"
    private val externalIdentifier: String = "qwerty"
    private val expectedUser = User(identifier = identifier, externalIdentifier = externalIdentifier)
    private val createUserData = CreateUserData(identifier)

    private val wireMockServer: WireMockServer
    private var authorizationManager: AuthorizationManager? = null

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
        authorizationManager = AuthorizationManager(URI("${wireMockServer.baseUrl()}/graphql"))
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `creates a user`() {
        val createdUser: User = authorizationManager?.create(createUserData) ?: throw RuntimeException()

        assertThat(createdUser).usingRecursiveComparison().ignoringFields("externalIdentifier").isEqualTo(expectedUser)
    }
}
