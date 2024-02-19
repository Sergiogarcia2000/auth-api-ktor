package dev.sergio.exceptions

class AuthenticationException: Exception(){
    override val message: String
        get() = "You are not authorized to access this resource"
}