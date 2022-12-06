package com.annti.repositoriesapp.presentation.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annti.repositoriesapp.data.model.RepositoryDownload
import com.annti.repositoriesapp.domain.FileManager
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val fileManager: FileManager
) : ViewModel() {

    private val errorLiveData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = errorLiveData
    private var downloadStateFlow = MutableStateFlow<List<RepositoryDownload>>(emptyList())
    val downloadList: StateFlow<List<RepositoryDownload>> = downloadStateFlow

    fun getDownloadRepository() {
        try {
            fileManager.getDownloadRepos()
                .onEach { downloadStateFlow.value = it }
                .launchIn(viewModelScope)

        } catch (e: Throwable) {
            errorLiveData.postValue("Произошла ошибка: ${e.message}")
        }
    }
}