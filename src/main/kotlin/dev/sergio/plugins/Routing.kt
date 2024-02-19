package dev.sergio.plugins

import dev.sergio.exceptions.AuthenticationException
import dev.sergio.routes.basic.basicRouting
import dev.sergio.routes.digest.digestRouting
import dev.sergio.routes.jwt.jwtRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is AuthenticationException)
                call.respondText(text = "401: ${cause.message}", status = HttpStatusCode.Unauthorized)
            else
                call.respondText(text = "500: ${cause.message}", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        basicRouting()
        digestRouting()
        jwtRouting()
    }
}
