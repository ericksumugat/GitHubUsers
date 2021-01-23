package com.githubusers.android.data.exceptions.extensions

import com.githubusers.android.data.exceptions.network.NoNetworkException

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**\
 * Map network call exceptions to application recognizable exceptions
 */

fun <T> Observable<T>.mapNetworkErrors(): Observable<T> {
    return this.onErrorResumeNext { error: Throwable ->
        when (error) {
            is ConnectException -> Observable.error(NoNetworkException(error))
            is SocketTimeoutException -> Observable.error(NoNetworkException(error))
            is UnknownHostException -> Observable.error(NoNetworkException(error))
            is HttpException -> Observable.error(error.toNetworkException())
            else -> Observable.error(error)
        }
    }
}


fun <T> Flowable<T>.mapNetworkErrors(): Flowable<T> {
    return this.onErrorResumeNext { error: Throwable ->
        when (error) {
            is ConnectException -> Flowable.error(NoNetworkException(error))
            is SocketTimeoutException -> Flowable.error(NoNetworkException(error))
            is UnknownHostException -> Flowable.error(NoNetworkException(error))
            is HttpException -> Flowable.error(error.toNetworkException())
            else -> Flowable.error(error)
        }
    }
}

fun <T> Single<T>.mapNetworkErrors(): Single<T> {
    return this.onErrorResumeNext { error: Throwable ->
        when (error) {
            is ConnectException -> Single.error(NoNetworkException(error))
            is SocketTimeoutException -> Single.error(NoNetworkException(error))
            is UnknownHostException -> Single.error(NoNetworkException(error))
            is HttpException -> Single.error(error.toNetworkException())
            else -> Single.error(error)
        }
    }
}

fun Completable.mapNetworkErrors(): Completable {
    return this.onErrorResumeNext { error ->
        when (error) {
            is ConnectException -> Completable.error(NoNetworkException(error))
            is SocketTimeoutException -> Completable.error(NoNetworkException(error))
            is UnknownHostException -> Completable.error(NoNetworkException(error))
            is HttpException -> Completable.error(error.toNetworkException())
            else -> Completable.error(error)
        }
    }
}
