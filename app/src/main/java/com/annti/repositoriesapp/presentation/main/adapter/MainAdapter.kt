package com.annti.repositoriesapp.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.DownloadListener
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.annti.repositoriesapp.data.model.Repository
import com.annti.repositoriesapp.databinding.ItemRepositoryBinding
import com.bumptech.glide.Glide

class MainAdapter(private val listener: RepositoryListener): PagingDataAdapter<Repository, RepoViewHolder>(MovieDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }
}

class MovieDiffCallBack : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }
}

class RepoViewHolder(
    private val binding: ItemRepositoryBinding,
    private val listener: RepositoryListener
) : RecyclerView.ViewHolder(binding.root) {



    fun bind(repo: Repository) {
        Glide.with(itemView)
            .load(repo.owner?.avatar)
            .circleCrop()
            .into(binding.imageView)
        binding.txtName.text = repo.name
        binding.txtId.text = repo.owner?.name
        binding.downloadRepo.setOnClickListener {
            listener.downloadRepository(repo)
        }
        binding.buttonOpenBrowser.setOnClickListener {
            listener.openInBrowser(repo)
        }
    }
}

interface RepositoryListener{
    fun downloadRepository(repo: Repository)
    fun openInBrowser(repo:Repository)
}