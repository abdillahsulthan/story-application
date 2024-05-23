package com.dicoding.storyapp.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.ui.adapter.LoadingStateAdapter
import com.dicoding.storyapp.ui.adapter.StoryAdapter
import com.dicoding.storyapp.ui.auth.AuthenticationActivity
import com.dicoding.storyapp.ui.auth.AuthenticationViewModel
import com.dicoding.storyapp.ui.auth.AuthenticationViewModelFactory
import com.dicoding.storyapp.ui.maps.MapsActivity
import com.dicoding.storyapp.ui.uploadstory.UploadStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        authenticationViewModel.getSession().observe(this) {
            if (!it.isLogin) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }
            else {
                getData(it.token)
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        playAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            R.id.action_map -> {
                mapActivity()
                true
            }
            else -> false
        }
    }

    private fun mapActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.logout_title)
        builder.setMessage(R.string.confirm_logout)
        builder.setPositiveButton(R.string.confirmed_logout) { _, _ ->
            authenticationViewModel.logout()
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton(R.string.not_logout) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun getData(token: String) {
        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        val mainViewModel by viewModels<MainViewModel> {
            MainViewModelFactory.getInstance(this, token)
        }
        mainViewModel.getStories().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun playAnimation() {
        val rvStories = ObjectAnimator.ofFloat(binding.rvStories, View.ALPHA, 1f).setDuration(500)
        val fab = ObjectAnimator.ofFloat(binding.fab, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playTogether(rvStories, fab)
            startDelay = 500
        }.start()
    }
}