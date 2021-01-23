package com.githubusers.android.presentation.utils

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.lang.Exception

fun ImageView.loadUrl(url: String, placeHolderId: Int = 0, transformation: Transformation? = null) {
    if (url.isEmpty()) {
        return
    }

    val requestCreator = Picasso.get().load(url)
    if (placeHolderId != 0) {
        requestCreator.placeholder(placeHolderId)
    }
    transformation?.let {
        requestCreator.transform(it)
    }
    requestCreator.into(this)
}

fun ImageView.loadUrl(url: String, placeHolderId: Int = 0, listener: (Boolean) -> Unit) {
    if (url.isEmpty()) {
        return
    }

    val requestCreator = Picasso.get().load(url)
    if (placeHolderId != 0) {
        requestCreator.placeholder(placeHolderId)
    }
    requestCreator.into(this, object: Callback {

        override fun onSuccess() { listener.invoke(true) }

        override fun onError(e: Exception?) { listener.invoke(false) }

    })
}