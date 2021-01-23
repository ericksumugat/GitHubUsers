package com.githubusers.android.data.exceptions.extensions

import com.githubusers.android.data.exceptions.network.NetworkException
import retrofit2.HttpException

fun HttpException.toNetworkException(): NetworkException {
    val serverErrorResponse = this.response()?.errorBody()?.string()
    val rawCode = this.code()
    return NetworkException(rawCode, serverErrorResponse, this)
}
