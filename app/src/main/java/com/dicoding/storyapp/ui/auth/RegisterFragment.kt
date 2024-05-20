package com.dicoding.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        playAnimation()
        setupAction()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val name = binding.edRegisterName.text.toString()

            if (TextUtils.isEmpty(email)) {
                binding.edRegisterEmail.error = "Field must be filled"
            } else if (TextUtils.isEmpty(password)) {
                binding.edRegisterPassword.error = "Field must be filled"
            } else if (TextUtils.isEmpty(name)) {
                binding.edRegisterName.error = "Field must be filled"
            } else if ((binding.edRegisterPassword.error?.length ?: 0) > 0) {
                binding.edRegisterPassword.requestFocus()
            } else {
                authViewModel.register(name = name, password = password, email = email)
                authViewModel.registerUser.observe(viewLifecycleOwner) {
                    if (!it.error) {
                        Toast.makeText(this.context, "Register successful", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.authentication_container, LoginFragment(), LoginFragment::class.java.simpleName)
                            commit()
                        }
                    }
                    else {
                        Toast.makeText(this.context, "Register failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.authentication_container, LoginFragment(), LoginFragment::class.java.simpleName)
                commit()
            }
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(500)
        val welcomeRegister = ObjectAnimator.ofFloat(binding.welcomeRegister, View.ALPHA, 1f).setDuration(500)
        val registerInfo = ObjectAnimator.ofFloat(binding.registerInfo, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val signUp = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginContainer, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(logo,welcomeRegister, registerInfo, name, email, password, signUp, login)
        }.start()
    }
}