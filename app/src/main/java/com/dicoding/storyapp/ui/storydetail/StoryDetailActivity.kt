package com.dicoding.storyapp.ui.storydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)

        supportActionBar?.title = "Story Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(intent.getStringExtra(EXTRA_PHOTO)).into(binding.ivDetailPhoto)
        binding.tvDetailName.text = intent.getStringExtra(EXTRA_NAME)
        binding.tvDetailDescription.text = intent.getStringExtra(EXTRA_DESCRIPTION)

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}