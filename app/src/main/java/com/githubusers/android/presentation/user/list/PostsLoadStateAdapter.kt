package com.githubusers.android.presentation.user.list

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class PostsLoadStateAdapter() : LoadStateAdapter<LoadingItemViewHolder>() {

    override fun onBindViewHolder(holder: LoadingItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoadingItemViewHolder {
        return LoadingItemViewHolder.create(parent)
    }


}