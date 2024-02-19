package dev.sergio.auth

import dev.sergio.exceptions.AuthenticationException
import io.ktor.server.auth.*
import io.ktor.util.*

object BasicAuthTypes {
    const val BASIC_AUTH = "auth-basic"
    const val BASIC_AUTH_HASHED = "auth-basic-hashed"
}

data class BasicUser(val name: String) : Principal

fun AuthenticationConfig.configureBasic() {

    val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
    val hashedUserTable = UserHashedTableAuth(
        table = mapOf(
            "sergio" to digestFunction("1234"),
            "daniel" to digestFunction("4321")
        ),
        digester = digestFunction
    )

    basic(BasicAuthTypes.BASIC_AUTH) {
        realm = "Ktor Server"
        validate { credentials ->
            if (credentials.name == credentials.password) {
                BasicUser(credentials.name)
            } else {
                throw AuthenticationException()
            }
        }
    }

    basic (BasicAuthTypes.BASIC_AUTH_HASHED){
        realm = "Ktor Server"
        validate { credentials ->
            BasicUser(hashedUserTable.authenticate(credentials)?.name ?: throw AuthenticationException())
        }
    }
}