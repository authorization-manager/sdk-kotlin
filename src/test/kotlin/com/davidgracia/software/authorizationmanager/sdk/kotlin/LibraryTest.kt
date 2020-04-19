package com.davidgracia.software.authorizationmanager.sdk.kotlin

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {
    @Test
    fun testSomeLibraryMethod() {
        UUID.randomUUID()
        val user = User("1234")
        user.boo()
        AuthorizationManager().save(user)
        assertEquals(expected = user, actual = user)
    }
}
