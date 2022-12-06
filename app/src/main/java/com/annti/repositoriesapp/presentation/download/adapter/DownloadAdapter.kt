package com.annti.repositoriesapp.presentation.download.adapter

import androidx.recyclerview.widget.DiffUtil
import com.annti.repositoriesapp.data.model.RepositoryDownload
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class DownloadAdapter(itemSelected: DownloadAdapterDelegate.ItemSelected) :
    AsyncListDifferDelegationAdapter<RepositoryDownload>(ApplicationDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(DownloadAdapterDelegate(itemSelected))
    }

    class ApplicationDiffUtilCallback : DiffUtil.ItemCallback<RepositoryDownload>() {
        override fun areItemsTheSame(
            oldItem: RepositoryDownload,
            newItem: RepositoryDownload
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RepositoryDownload,
            newItem: RepositoryDownload
        ): Boolean {
            return oldItem == newItem
        }
    }
}