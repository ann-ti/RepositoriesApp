package com.annti.repositoriesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.annti.repositoriesapp.data.model.AccessToken
import com.annti.repositoriesapp.data.model.Owner
import com.annti.repositoriesapp.data.model.Repository

@Database(
    entities = [
        AccessToken::class,
        Repository::class,
        Owner::class
    ], version = MainDatabase.DB_VERSION
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun mainDao(): AuthDao
    abstract fun repoDao(): RepoDao

    companion object {
        const val DB_VERSION = 1
        const val DB_MAME = "main_database"
    }
}