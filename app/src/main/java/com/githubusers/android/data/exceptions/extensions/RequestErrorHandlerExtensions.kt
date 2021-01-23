package com.githubusers.android.data.exceptions.extensions

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import kotlin.math.pow

/**
 * Retries the operation with exponential backoff
 *
 * [maxRetry] - the max number of retry to attempt.
 */
fun <T> Observable<T>.retryOnError(maxRetry: Int = 0): Observable<T> {
    return this.retryWhen {
        it.zipWith(Observable.range(1, (maxRetry + 1))) { throwable, retryCount ->
            Pair(
                throwable,
                retryCount
            )
        }.flatMap { pair ->
            val throwable = pair.first
            val retryCount = pair.second

            if (isErrorRetrieable(throwable)){
                val delay = 1.5.pow((retryCount + 1).toDouble()).toLong()

                println("max $maxRetry retryCount: $retryCount Delay: $delay")

                if (retryCount > maxRetry) {
                    // Retries exhausted
                    Observable.error(throwable)
                } else {
                    Observable.timer(delay, TimeUnit.SECONDS)
                        .flatMap { Observable.just(pair) }
                }
            } else {
                Observable.error(throwable)
            }
        }
    }
}

private fun isErrorRetrieable(throwable: Throwable): Boolean{
    /*
     * TODO: Determine what kind of error to retry. Will leave "true" for now for presentation purposes.
     */
    return true
}
