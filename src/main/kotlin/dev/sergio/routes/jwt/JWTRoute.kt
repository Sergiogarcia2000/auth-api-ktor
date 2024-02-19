package dev.sergio.routes.jwt

import dev.sergio.auth.JWTAuth
import dev.sergio.auth.createToken
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class User(val username: String, val password: String)

fun Routing.jwtRouting() {
    route("/JWT") {

        post("/login") {
            val user = call.receive<User>()
            if (user.username == user.password) {
                val token = createToken(user.username)
                call.respondText(token)
            } else {
                call.respondText("Invalid user")
            }
        }

        authenticate(JWTAuth.JWT_AUTH_NAME) {
            get {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}