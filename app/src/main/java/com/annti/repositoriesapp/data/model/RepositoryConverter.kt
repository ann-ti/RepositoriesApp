package com.annti.repositoriesapp.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class RepositoryConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromOwnerToString(owner: Owner): String? {
        return gson.toJson(owner)
    }

    @TypeConverter
    fun fromStringToOwner(ownerS: String): Owner? {
        return gson.fromJson(ownerS, Owner::class.java)
    }
}
