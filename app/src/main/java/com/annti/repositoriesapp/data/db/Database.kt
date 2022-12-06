package com.annti.repositoriesapp.data.db

import android.content.Context
import androidx.room.Room

object Database {

    lateinit var instance: MainDatabase
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            MainDatabase::class.java,
            MainDatabase.DB_MAME
        )
            .build()
    }
}