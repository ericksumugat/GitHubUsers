package com.githubusers.android.data.exceptions.network

import com.githubusers.android.data.exceptions.ApplicationException


open class NetworkException : ApplicationException {

    var statusCode: Int = 0
    var errorMessage: String? = null

    constructor(statusCode: Int, errorMessage: String?, error: Throwable) : super(error) {
        this.statusCode = statusCode
        this.errorMessage = errorMessage
    }

    constructor(statusCode: Int, error: Throwable) : super(error) {
        this.statusCode = statusCode
    }

    constructor(error: Throwable) : super(error)

    
}