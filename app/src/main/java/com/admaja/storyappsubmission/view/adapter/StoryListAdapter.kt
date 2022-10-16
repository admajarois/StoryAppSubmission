package com.admaja.storyappsubmission.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.databinding.StoryItemBinding
import com.admaja.storyappsubmission.view.detail.DetailStoryActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class StoryListAdapter: PagingDataAdapter<StoryEntity, StoryListAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class ItemViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(resultStory: StoryEntity) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(resultStory.photoUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.image_thumbnail))
                    .into(ivStory)
                tvItemCreateStory.text = resultStory.name
                tvItemOverview.text = resultStory.description
                itemView.setOnClickListener {
                    val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(ivStory, "image"),
                        Pair(tvItemCreateStory, "name")
                    )
                    Intent(itemView.context, DetailStoryActivity::class.java).apply {
                        putExtra(EXTRA_STORY, resultStory)
                        itemView.context.startActivity(
                            this
                        )
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> =
            object : DiffUtil.ItemCallback<StoryEntity>() {
                override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: StoryEntity,
                    newItem: StoryEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
        const val EXTRA_STORY = "extra_story"
    }
}