package com.davidgracia.software.authorizationmanager.sdk.kotlin

import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {
    @Test
    fun testSomeLibraryMethod() {
        val user = User("1234")
        user.boo()
        AuthorizationManager().save(user)
        assertEquals(expected = user, actual = user)
    }
}

