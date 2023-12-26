package com.globant.imdb.core

open class ErrorFactory(errorCode: Int) : ErrorData {
    private val error =
        ErrorStatusCode.values().firstOrNull { statusCode -> statusCode.code == errorCode }
            ?: ErrorStatusCode.Unknown

    override val message: String = (error.name.split("(?=\\p{Lu})".toRegex()).joinToString(" ").lowercase()).trim()
    override val code: Int = errorCode
}

interface ErrorData {
    val code: Int
    val message: String
}

enum class ErrorStatusCode(val code: Int) {
    // Custom Error
    Unknown(101),
    NoInternetConnection(102),
    TheUserAlreadyExists(103),
    IncorrectUserOrPassword(104),
    UserWithoutSession(105),
    SessionHasExpired(106),
    NoActiveSession(107),
    EmptyCache(108),

    // Client Errors
    BadRequest(400),
    Unauthorized(401),
    PaymentRequired(402),
    Forbidden(403),
    NotFound(404),

    // Server Errors
    InternalServerError(500),
    NotImplemented(501),
    BadGateway(502),
    ServiceUnavailable(503),
    GatewayTimeout(504),
    HttpVersionNotSupported(505),
    VariantAlsoNegates(506),
    InsufficientStorage(507),
    LoopDetected(508),
    NotExtended(510),
    NetworkAuthenticationRequired(511)
}