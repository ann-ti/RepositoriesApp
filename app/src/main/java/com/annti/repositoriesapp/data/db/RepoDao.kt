package com.annti.repositoriesapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.annti.repositoriesapp.data.model.Repository

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<Repository>)

    @Query("DELETE FROM repository")
    suspend fun deleteRepositories()

    @Query("SELECT * FROM repository")
    fun getRepositories(): androidx.paging.DataSource.Factory<Int, Repository>
}