package com.githubusers.android.data.sources.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey val id: Int,
    val login: String = "",
    val avatarUrl: String = "",
    val eventsUrl: String = "",
    val followersUrl: String = "",
    val followingUrl: String = "",
    val gistsUrl: String = "",
    val gravatarId: String = "",
    val htmlUrl: String = "",
    val nodeId: String = "",
    val organizationsUrl: String = "",
    val receivedEventsUrl: String = "",
    val reposUrl: String = "",
    val siteAdmin: Boolean = false,
    val starredUrl: String = "",
    val subscriptionsUrl: String = "",
    val type: String = "",
    val url: String = "",
    var note: String? = null,
    var followers: Int = 0,
    var following: Int = 0,
    var name: String? = null,
    var company: String? = null,
    var blog: String? = null,
    var location: String? = null,
    var email: String? = null,
    var twitterUserName: String? = null,
)

/**
 * Strategy for updating some fields that are not always being returned by services to avoid resetting them to default values.
 */
fun UserEntity.updateContent(newContent: UserEntity): UserEntity {
    if (newContent.note.isNullOrBlank()) {
        newContent.note = this.note
    }

    if (newContent.name.isNullOrBlank()) {
        newContent.name = this.name
    }

    if (newContent.company.isNullOrBlank()) {
        newContent.company = this.company
    }

    if (newContent.blog.isNullOrBlank()) {
        newContent.blog = this.blog
    }

    if (newContent.location.isNullOrBlank()) {
        newContent.location = this.location
    }

    if (newContent.email.isNullOrBlank()) {
        newContent.email = this.email
    }

    if (newContent.twitterUserName.isNullOrBlank()) {
        newContent.twitterUserName = this.twitterUserName
    }

    if (newContent.followers == 0) {
        newContent.followers = this.followers
    }

    if (newContent.following == 0) {
        newContent.following = this.following
    }

    return newContent
}

