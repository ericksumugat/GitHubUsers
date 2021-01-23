package com.githubusers.android.presentation.user.list

import android.graphics.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.githubusers.android.R
import com.githubusers.android.data.sources.local.UserEntity
import com.githubusers.android.presentation.utils.loadUrl
import com.squareup.picasso.Transformation
import de.hdodenhof.circleimageview.CircleImageView


class UserItemViewHolder(view: View, itemClickListener: UserListAdapter.OnItemClickListener) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup, itemClickListener: UserListAdapter.OnItemClickListener): UserItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_list_item, parent, false)
            return UserItemViewHolder(view, itemClickListener)
        }
    }

    init {
        view.setOnClickListener {
            user?.let { itemClickListener.onItemClick(user = it)  }
        }
    }


    private val ivProfile: CircleImageView = view.findViewById(R.id.iv_profile_image)
    private val tvUserName: AppCompatTextView = view.findViewById(R.id.tv_name)
    private val ivNote: AppCompatImageView = view.findViewById(R.id.iv_note)
    private var user: UserEntity? = null

    fun bind(userEntity: UserEntity, position: Int) {
        user = userEntity
        tvUserName.text = userEntity.login
        userEntity.note?.let { ivNote.visibility = View.VISIBLE } ?: run { ivNote.visibility = View.GONE }

        val result: Double = (position + 1) / 4.0
        if (result % 1.0 == 0.0) {
            Log.d("here", "bind: " + userEntity.login)
            ivProfile.loadUrl(userEntity.avatarUrl, transformation = InvertColorTransformation())
        } else {
            ivProfile.loadUrl(userEntity.avatarUrl)
        }
    }

    inner class InvertColorTransformation: Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val colorMatrixInverted = ColorMatrix(floatArrayOf(-1f, 0f, 0f, 0f, 255f, 0f, -1f, 0f, 0f, 255f, 0f, 0f, -1f, 0f, 255f, 0f, 0f, 0f, 1f, 0f))
            val colorFilterSepia: ColorFilter = ColorMatrixColorFilter(
                colorMatrixInverted)
            val bitmap = Bitmap.createBitmap(source.width, source.height,
                Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.colorFilter = colorFilterSepia
            canvas.drawBitmap(source, 0f, 0f, paint)
            source.recycle()
            return bitmap
        }

        override fun key(): String = "invert"
    }
}