package com.dicoding.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentLoginBinding
import com.dicoding.storyapp.ui.main.MainActivity
import com.dicoding.storyapp.data.preference.User

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
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
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                binding.edLoginEmail.error = "Field must be filled"
            } else if (TextUtils.isEmpty(password)) {
                binding.edLoginPassword.error = "Field must be filled"
            } else if ((binding.edLoginPassword.error?.length ?: 0) > 0) {
                binding.edLoginPassword.requestFocus()
            } else {
                authViewModel.login(email, password)
                authViewModel.loginUser.observe(viewLifecycleOwner) {
                    if (!it.error) {
                        Toast.makeText(this.context, "Login successful", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this.context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            authViewModel.loginUser.observe(viewLifecycleOwner) {
                authViewModel.saveSession(User(email, it.loginResult.name, it.loginResult.userId, it.loginResult.token))
            }
        }

        authViewModel.getSession().observe(viewLifecycleOwner) {
            if (it.isLogin) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnRegister.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.authentication_container, RegisterFragment(), RegisterFragment::class.java.simpleName)
            }
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(500)
        val welcomeLogin = ObjectAnimator.ofFloat(binding.welcomeLogin, View.ALPHA, 1f).setDuration(500)
        val loginInfo = ObjectAnimator.ofFloat(binding.loginInfo, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val signIn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerContainer, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(logo,welcomeLogin, loginInfo, email, password, signIn,register)
        }.start()
    }
}