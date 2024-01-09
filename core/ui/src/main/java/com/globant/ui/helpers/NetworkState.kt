package com.globant.ui.helpers

sealed interface NetworkState {
    data object Loading: NetworkState
    data object Online: NetworkState
    data object Offline: NetworkState
}