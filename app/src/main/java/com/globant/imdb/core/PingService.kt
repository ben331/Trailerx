package com.globant.imdb.core

import javax.inject.Inject

class PingService @Inject constructor() {
    fun isConnected(): Boolean {
        val command = "ping -c 1 ${Constants.API_SERVER_NAME}"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }
}