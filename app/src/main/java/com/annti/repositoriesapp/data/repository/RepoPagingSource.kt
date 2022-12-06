package com.annti.repositoriesapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.annti.repositoriesapp.data.model.Repository
import com.annti.repositoriesapp.data.network.api.RepoApi
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 20

class RepoPagingSource(
    private val repoApi: RepoApi,
    private val query:String
) : PagingSource<Int, Repository>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = repoApi.searchQueryRepositories(query)
            val nextKey =
                if (response.repositories.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = response.repositories,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}