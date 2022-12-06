package com.annti.repositoriesapp.presentation.download.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annti.repositoriesapp.R
import com.annti.repositoriesapp.data.model.RepositoryDownload
import com.annti.repositoriesapp.databinding.ItemDownloadBinding
import com.annti.repositoriesapp.utils.inflate
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class DownloadAdapterDelegate(private val itemSelected: ItemSelected) :
    AbsListItemAdapterDelegate<RepositoryDownload, RepositoryDownload, DownloadAdapterDelegate.Holder>() {

    override fun isForViewType(
        item: RepositoryDownload,
        items: MutableList<RepositoryDownload>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_download), itemSelected)
    }

    override fun onBindViewHolder(
        item: RepositoryDownload,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        private val view: View,
        private val itemSelected: ItemSelected
    ) : RecyclerView.ViewHolder(view) {
        private val binding = ItemDownloadBinding.bind(view)

        fun bind(repositoryDownload: RepositoryDownload) {
            binding.txtName.text = repositoryDownload.name
            binding.buttonOpenFile.setOnClickListener {
                itemSelected.onItemSelected(repositoryDownload)
            }
        }
    }

    interface ItemSelected {
        fun onItemSelected(item: RepositoryDownload)
    }
}