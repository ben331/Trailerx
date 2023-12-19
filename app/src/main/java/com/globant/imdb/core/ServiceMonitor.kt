package com.globant.imdb.core

import javax.inject.Inject

class ServiceMonitor @Inject constructor() {
    fun isConnected(): Boolean {
        val command = "curl -I ${Constants.API_SERVER_NAME}"
        return  Runtime.getRuntime().exec(command).waitFor() == 0
    }
}