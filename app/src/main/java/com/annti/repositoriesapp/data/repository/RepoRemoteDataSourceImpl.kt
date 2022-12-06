package com.annti.repositoriesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.annti.repositoriesapp.data.model.Repository
import com.annti.repositoriesapp.data.network.api.RepoApi
import com.annti.repositoriesapp.utils.handleNetworkErrors
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.http.Path

interface RepoRemoteDataSource {
    fun getRepositories(query: String): Flow<PagingData<Repository>>
    suspend fun downloadRepository(owner: String, repo: String, ref: String): ByteArray
}

internal class RepoRemoteDataSourceImpl(
    private val repoApi: RepoApi
) : RepoRemoteDataSource {

    override fun getRepositories(query: String): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RepoPagingSource(repoApi, query)
            }
        ).flow
    }

    override suspend fun downloadRepository(owner: String, repo: String, ref: String): ByteArray =
        handleNetworkErrors {
            repoApi.downloadRepository(owner, repo, ref).bytes()
        }

}