package com.example.myshop.data

data class LoadingState(val status: Status, val msg: String? = null) {
    companion object {
        val IDLE = LoadingState(status = Status.IDLE)
        val SUCCESS = LoadingState(status = Status.SUCCESS)
        val FAIL = LoadingState(status = Status.FAIL)
        val LOADING = LoadingState(status = Status.LOADING)
    }

    enum class Status {
        IDLE,
        SUCCESS,
        FAIL,
        LOADING
    }
}