package com.davidgracia.software.authorizationmanager.sdk.kotlin

import java.net.URI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class AuthorizationManagerTest {

    private val authorizationManager = AuthorizationManager(URI("http://localhost:8080/graphql"))
    private val identifier = "1234"
    private val externalIdentifier: String = "qwerty"
    private val expectedUser = User(identifier = identifier, externalIdentifier = externalIdentifier)
    private val createUserData = CreateUserData(identifier)

    @Test
    fun `creates a user`() {
        val createdUser: User = authorizationManager.create(createUserData)

        assertThat(createdUser).usingRecursiveComparison().ignoringFields("externalIdentifier").isEqualTo(expectedUser)
    }
}
