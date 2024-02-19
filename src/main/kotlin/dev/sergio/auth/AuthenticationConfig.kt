package dev.sergio.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication() {
    authentication {
        configureBasic()
        configureDigest()
        configureJWT()
    }
}