package com.githubusers.android.data.exceptions

import java.lang.RuntimeException

open class ApplicationException: RuntimeException {

    constructor(throwable: Throwable): super(throwable)
    constructor(message: String): super(message)
    constructor(message: String, throwable: Throwable): super(message, throwable)
}

