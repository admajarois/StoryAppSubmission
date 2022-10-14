package com.admaja.storyappsubmission.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.admaja.storyappsubmission.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateAdapterViewHolder>() {

    override fun onBindViewHolder(holder: LoadingStateAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateAdapterViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context))
        return LoadingStateAdapterViewHolder(binding, retry)
    }

    class LoadingStateAdapterViewHolder(
        private val binding: ItemLoadingBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvErrorMsg.text = loadState.error.localizedMessage
            }
            binding.pbListStory.isVisible = loadState is LoadState.Loading
            binding.btRetry.isVisible = loadState is LoadState.Error
            binding.tvErrorMsg.isVisible = loadState is LoadState.Error
        }
    }
}