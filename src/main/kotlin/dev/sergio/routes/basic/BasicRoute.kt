package dev.sergio.routes.basic

import dev.sergio.auth.BasicAuthTypes
import dev.sergio.auth.BasicUser
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.basicRouting() {
    route("/basic") {

        authenticate(BasicAuthTypes.BASIC_AUTH) {
            get {
                call.respondText("Hello, ${call.principal<BasicUser>()?.name}!")
            }
        }

        authenticate(BasicAuthTypes.BASIC_AUTH_HASHED) {
            get("/hashed") {
                call.respondText("Hello, ${call.principal<BasicUser>()?.name}!")
            }
        }
    }
}
