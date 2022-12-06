package com.annti.repositoriesapp.presentation.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.annti.repositoriesapp.data.model.Repository
import com.annti.repositoriesapp.data.repository.RepoRemoteDataSource
import com.annti.repositoriesapp.domain.FileManager
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repoDataSource: RepoRemoteDataSource,
    private val fileManager: FileManager
) : ViewModel() {

    private val openFileLiveData = LiveEvent<Uri>()
    val openFile: LiveData<Uri> get() = openFileLiveData
    private val errorLiveData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = errorLiveData

    fun getRepositories(query: String): Flow<PagingData<Repository>> {
        return repoDataSource.getRepositories(query)
            .cachedIn(viewModelScope)
    }

    fun downloadRepository(repository: Repository) {
        viewModelScope.launch {
            try {
                val repositoryData = repoDataSource.downloadRepository(
                    repository.owner?.name!!,
                    repository.name!!,
                    "master"
                )
                val uri = withContext(Dispatchers.Default) {
                    val fileName = "${repository.fullName}.zip"
                    fileManager.writeToPDF(fileName, repositoryData)
                }
                openFileLiveData.postValue(uri)
            } catch (e: Throwable) {
                errorLiveData.postValue("Произошла ошибка: ${e.message}")
            }
        }
    }
}