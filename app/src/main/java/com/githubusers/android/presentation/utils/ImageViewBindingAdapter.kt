package com.githubusers.android.presentation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let { view.loadUrl(it)  }

}