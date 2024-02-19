package dev.sergio.routes.digest

import dev.sergio.auth.DigestAuthTypes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.digestRouting() {
    route("/digest") {

        authenticate(DigestAuthTypes.DIGEST_AUTH) {
            get {
                call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
            }
        }
    }
}