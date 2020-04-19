/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.davidgracia.software.authorizationmanager.sdk.kotlin

import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {
    @Test
    fun testSomeLibraryMethod() {
        val user = User("1234")
        user.boo()
        AuthorizationManagerSDK().save(user)
        assertEquals(expected = user, actual = user)
    }
}

