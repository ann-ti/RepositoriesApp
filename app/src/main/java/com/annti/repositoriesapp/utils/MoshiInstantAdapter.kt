package com.annti.repositoriesapp.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class MoshiInstantAdapter {
    @ToJson
    fun toJson(instant: Instant): String = instant.toString()

    @FromJson
    fun fromJson(s: String): Instant = Instant.parse(s)
}