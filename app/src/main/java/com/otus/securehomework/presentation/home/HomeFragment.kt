package com.otus.securehomework.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.otus.securehomework.R
import com.otus.securehomework.data.Response
import com.otus.securehomework.data.dto.User
import com.otus.securehomework.databinding.FragmentHomeBinding
import com.otus.securehomework.presentation.handleApiError
import com.otus.securehomework.presentation.logout
import com.otus.securehomework.presentation.visible
import com.otus.securehomework.security.SecureBiometric
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    @Inject
    lateinit var biometricHelper: SecureBiometric

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setupViews()
        observeBiometricAuth()
        observeUserData()
        setupLogoutButton()
    }

    private fun setupViews() {
        binding.progressbar.visible(false)
    }

    private fun observeBiometricAuth() {
        biometricHelper.isBiometricAuthEnabled.asLiveData().observe(viewLifecycleOwner) { isEnabled ->
            binding.biometricSwitch.isChecked = isEnabled
            binding.biometricSwitch.setOnCheckedChangeListener { _, isChecked ->
                lifecycleScope.launch {
                    biometricHelper.enableBiometricAuth(isChecked)
                }
            }
        }
    }

    private fun observeUserData() {
        viewModel.user.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.progressbar.visible(false)
                    updateUI(response.value.user)
                }

                is Response.Loading -> {
                    binding.progressbar.visible(true)
                }

                is Response.Failure -> {
                    handleApiError(response)
                }
            }
        }
    }

    private fun setupLogoutButton() {
        binding.buttonLogout.setOnClickListener {
            logout()
        }
    }

    private fun updateUI(user: User) {
        with(binding) {
            textViewId.text = user.id.toString()
            textViewName.text = user.name
            textViewEmail.text = user.email
        }
    }
}
