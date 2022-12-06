package com.annti.repositoriesapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String
) : Parcelable

@Entity(tableName = "access_token")
@JsonClass(generateAdapter = true)
data class AccessToken(
    val token: String? = null,
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0
)

object AccessTokenDto {
    fun fromResponse(response: AuthResponse) = AccessToken(response.accessToken)
}