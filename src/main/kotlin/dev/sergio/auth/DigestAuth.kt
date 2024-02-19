package dev.sergio.auth

import dev.sergio.exceptions.AuthenticationException
import io.ktor.server.auth.*
import java.security.MessageDigest

object DigestAuthTypes {
    const val DIGEST_AUTH = "digest-auth"
}

fun AuthenticationConfig.configureDigest() {
    fun getMd5Digest(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray())
    val myRealm = "MyRealm"
    val userTable: Map<String, ByteArray> = mapOf(
        "sergio" to getMd5Digest("sergio:${myRealm}:1234"),
        "daniel" to getMd5Digest("daniel:${myRealm}:4321")
    )

    digest(DigestAuthTypes.DIGEST_AUTH) {
        realm = myRealm
        digestProvider { userName, realm ->
            userTable[userName]
        }
        validate { credentials ->
            if (credentials.userName.isNotEmpty()) {
                UserIdPrincipal(credentials.userName)
            } else {
                throw AuthenticationException()
            }
        }
    }
}