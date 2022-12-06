package com.annti.repositoriesapp.presentation.download

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.annti.repositoriesapp.R
import com.annti.repositoriesapp.data.model.RepositoryDownload
import com.annti.repositoriesapp.databinding.FragmentDownloadBinding
import com.annti.repositoriesapp.presentation.download.adapter.DownloadAdapter
import com.annti.repositoriesapp.presentation.download.adapter.DownloadAdapterDelegate
import com.annti.repositoriesapp.utils.openFile
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadFragment : Fragment(R.layout.fragment_download),
    DownloadAdapterDelegate.ItemSelected {

    private lateinit var binding: FragmentDownloadBinding
    private val viewModel by viewModel<DownloadViewModel>()
    private val adapterGallery: DownloadAdapter by lazy { DownloadAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setList()
        getDownloadList()
    }

    private fun setList() {
        with(binding.downloadRepositoryRecyclerView) {
            adapter = adapterGallery
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }
    }

    private fun getDownloadList() {
        viewModel.getDownloadRepository()
        lifecycleScope.launchWhenStarted {
            viewModel.downloadList.collect{
                adapterGallery.items = it
            }
        }
        viewModel.errorData
    }


    override fun onItemSelected(item: RepositoryDownload) {
        requireContext().openFile(item.uri, "application/zip")
    }

}