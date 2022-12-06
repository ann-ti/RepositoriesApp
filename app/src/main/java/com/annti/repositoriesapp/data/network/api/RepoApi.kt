package com.annti.repositoriesapp.data.network.api

import com.annti.repositoriesapp.data.model.RepoResponse
import com.annti.repositoriesapp.data.network.AuthRequired
import okhttp3.ResponseBody
import retrofit2.http.*

interface RepoApi {

    @AuthRequired
    @GET("/search/repositories")
    suspend fun searchQueryRepositories(
        @Query("q") query: String
    ): RepoResponse

    @GET("/repos/{owner}/{repo}/zipball/{ref}")
    suspend fun downloadRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("ref") ref: String
    ): ResponseBody

}