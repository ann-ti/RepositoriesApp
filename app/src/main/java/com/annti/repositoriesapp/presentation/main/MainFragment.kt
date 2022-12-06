package com.annti.repositoriesapp.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.annti.repositoriesapp.R
import com.annti.repositoriesapp.data.model.Repository
import com.annti.repositoriesapp.databinding.FragmentMainBinding
import com.annti.repositoriesapp.presentation.main.adapter.MainAdapter
import com.annti.repositoriesapp.presentation.main.adapter.RepositoryListener
import com.annti.repositoriesapp.utils.onQueryTextChange
import com.annti.repositoriesapp.utils.openFile
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main), RepositoryListener {

    private lateinit var binding: FragmentMainBinding

    private val mainViewModel: MainViewModel by viewModel()
    private val mainAdapter: MainAdapter by lazy { MainAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()

        mainViewModel.errorData.observe(viewLifecycleOwner) {
            Snackbar.make(
                binding.root,
                it,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun initView() {
        with(binding.searchRepositoryRecyclerView) {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun collectUiState() {
        val query = binding.searchView.onQueryTextChange()
        viewLifecycleOwner.lifecycleScope.launch {
            query.collectLatest {
                mainViewModel.getRepositories(it).collectLatest { repositories ->
                    mainAdapter.submitData(repositories)
                }
            }
        }
    }

    private fun openFile(uri: Uri) {
        requireContext().openFile(uri, "application/zip")
    }

    override fun downloadRepository(repo: Repository) {
        mainViewModel.downloadRepository(repo)
        mainViewModel.openFile.observe(viewLifecycleOwner) {
            openFile(it)
        }
    }

    override fun openInBrowser(repo: Repository) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.owner?.htmlUrl))
        startActivity(browserIntent)
    }

}