package com.annti.repositoriesapp.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class RepositoryDownload(
    val id: Long,
    val name: String,
    val uri: Uri
)

@JsonClass(generateAdapter = true)
data class RepoResponse(
    @Json(name = "total_count")
    val totalCount: Int = 0,
    @Json(name = "items")
    val repositories: List<Repository> = emptyList()
)

@Entity(tableName = "repository")
@JsonClass(generateAdapter = true)
@TypeConverters(RepositoryConverter::class)
data class Repository(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String? = null,
    @Json(name = "full_name")
    val fullName: String? = null,
    val description: String? = null,
    @Json(name = "fork")
    val isFork: Boolean? = null,
    @Json(name = "owner")
    val owner: Owner? = null,
    val watchers: Long? = null,
    val forks: Long? = null,
    @Json(name = "open_issues")
    val issues: Long? = null,
    @Json(name = "created_at")
    val createdAt: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String? = null,
    val language: String? = null,
    @Json(name = "downloads_url")
    val downloadsUrl: String? = null
)


@Entity(tableName = "owners")
@JsonClass(generateAdapter = true)
data class Owner(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @Json(name = "login")
    val name: String? = null,
    @Json(name = "avatar_url")
    val avatar: String? = null,
    @Json(name = "html_url")
    val htmlUrl: String? = null,
    val type: String? = null,
    @Json(name = "site_admin")
    val isSiteAdmin: Boolean? = null,
    val company: String? = null,
    val location: String? = null,
    val email: String? = null,
    val bio: String? = null,
    @Json(name = "created_at")
    val createdAt: String? = null
)
