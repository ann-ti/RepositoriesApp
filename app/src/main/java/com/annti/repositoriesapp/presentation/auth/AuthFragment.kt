package com.annti.repositoriesapp.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.annti.repositoriesapp.MainActivity
import com.annti.repositoriesapp.R
import com.annti.repositoriesapp.databinding.FragmentAuthBinding
import com.annti.repositoriesapp.utils.ResultData
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel by viewModel<AuthViewModel>()
    private lateinit var redirectUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAuthBinding.inflate(inflater, container, false).apply {
            redirectUrl =
                "repositoriesapp://callback"

            authenticateWithGithub.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(viewModel.getOAuthUrl(redirectUrl))
                    )
                )
            }
        }.root
    }

    override fun onResume() {
        super.onResume()
        val uri = requireActivity().intent.data
        if (viewModel.verifyOAuthResponse(uri, "repositoriesapp")) {
            val code = viewModel.getCode(uri!!)

            viewModel.getAccessToken(code!!, redirectUrl)
                .observe(viewLifecycleOwner) {
                    when (it) {
                        is ResultData.Loading -> {
                            //showLoadingDialog(R.string.getting_access_token)
                        }
                        is ResultData.Success -> {
                            //cancelLoadingScreen()
                            nextActivity(MainActivity::class.java)
                        }
                        is ResultData.Failure -> {
                            //cancelLoadingScreen()
                            showToastMessage(it.message)
                        }
                    }
                }
        }
    }

    private fun showToastMessage(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

    private fun nextActivity(clazz: Class<*>) {
        startActivity(Intent(requireActivity(), clazz))
        requireActivity().finish()
    }

}