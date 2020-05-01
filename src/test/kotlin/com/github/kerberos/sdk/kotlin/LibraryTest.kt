package com.github.kerberos.sdk.kotlin

import java.net.URI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class LibraryTest {

//    private val authorizationManager = AuthorizationManager(URI("http://localhost:8080/users"))
    private val authorizationManager = AuthorizationManager(URI("http://localhost:8080/graphql"))
    private val identifier = "1234"
    private val name = "John"
    private val user = User(identifier = identifier, name = name)
    private val expectedUser = User(identifier = identifier, name = name)

    @Disabled
    @Test
    fun testSomeLibraryMethod() {
        val createdUser: User = authorizationManager.save(user)

        assertThat(createdUser).usingRecursiveComparison().ignoringFields("identifier").isEqualTo(expectedUser)
    }

    @Disabled
    @Test
    fun testSomeLibraryMethod2() {
        authorizationManager.getUser("1")

//        assertThat(createdUser).usingRecursiveComparison().ignoringFields("identifier").isEqualTo(expectedUser)
    }
}
