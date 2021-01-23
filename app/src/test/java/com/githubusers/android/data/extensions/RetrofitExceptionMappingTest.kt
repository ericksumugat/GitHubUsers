package com.githubusers.android.data.extensions

import com.githubusers.android.data.exceptions.extensions.mapNetworkErrors
import com.githubusers.android.data.exceptions.network.NetworkException
import com.githubusers.android.data.exceptions.network.NoNetworkException
import io.mockk.MockKAnnotations
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

class RetrofitExceptionMappingTest {

    companion object {
        const val MEDIA_TYPE: String = "application/json"
    }


    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `map SocketTimeoutException to NoNetworkException test`() {
        Observable.fromCallable {
            throw SocketTimeoutException()
        }.mapNetworkErrors().test()
            .assertFailure(NoNetworkException::class.java).dispose()
    }

    @Test
    fun `map ConnectException to NoNetworkException test`() {
        Observable.fromCallable {
            throw ConnectException()
        }.mapNetworkErrors().test()
            .assertFailure(NoNetworkException::class.java).dispose()
    }


    @Test
    fun `HttpException to NetworkException`() {
        Observable.fromCallable {
            throw HttpException(
                Response.error<Any>(
                    504,
                    ResponseBody.create(MediaType.parse(MEDIA_TYPE), "Error Message")
                )
            )
        }.mapNetworkErrors().test()
            .assertFailure(NetworkException::class.java)
            .assertError {
                val networkException = it as NetworkException
                networkException.statusCode == 504 && networkException.errorMessage.equals("Error Message", true)
            }.dispose()
    }


}