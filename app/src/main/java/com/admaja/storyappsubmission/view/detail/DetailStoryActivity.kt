package com.admaja.storyappsubmission.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.databinding.ActivityDetailStoryBinding
import com.admaja.storyappsubmission.view.adapter.StoryListAdapter
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.getParcelableExtra<StoryEntity>(StoryListAdapter.EXTRA_STORY)
        binding.apply {
            tvStoryCreate.text = extras?.name
            tvStoryDescription.text = extras?.description
            Glide.with(this@DetailStoryActivity)
                .load(extras?.photoUrl)
                .placeholder(R.drawable.image_thumbnail)
                .into(ivDetailStory)
        }
    }
}