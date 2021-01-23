package com.githubusers.android.presentation.user.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.githubusers.android.data.sources.local.UserEntity

class UserListAdapter(private val itemClickListener: OnItemClickListener) :
    PagingDataAdapter<UserEntity, UserItemViewHolder>(USER_COMPARATOR) {

    interface OnItemClickListener {
        fun onItemClick(user: UserEntity)
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        return UserItemViewHolder.create(parent, itemClickListener)
    }

    companion object {

        val USER_COMPARATOR = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
                oldItem.login == newItem.login
        }
    }
}