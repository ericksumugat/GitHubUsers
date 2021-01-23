package com.githubusers.android.presentation.user.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.githubusers.android.R

class LoadingItemViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): LoadingItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item, parent, false)
            return LoadingItemViewHolder(view)
        }
    }

    private val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)


    fun bindTo(loadState: LoadState) {
        progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
    }
}