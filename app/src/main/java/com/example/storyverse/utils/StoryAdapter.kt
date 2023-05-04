package com.example.storyverse.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.storyverse.R
import com.example.storyverse.databinding.ViewholderStoryBinding
import com.example.storyverse.domain.entity.StoryEntity

class StoryAdapter (
    private val onItemClickedListener : OnItemClicked,
): PagingDataAdapter<StoryEntity, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK){

    inner class StoryViewHolder(private var binding: ViewholderStoryBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(story : StoryEntity){
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description

            Glide.with(itemView.context)
                .load(story.photoUri)
                .transform(CenterCrop(), RoundedCorners(40))
                .placeholder(R.drawable.ic_baseline_image_24)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivItemPhoto)

            itemView.setOnClickListener{
                onItemClickedListener.onClicked(story)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ViewholderStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val user = getItem(position)
        if(user!=null){
            holder.bind(user)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> =
            object : DiffUtil.ItemCallback<StoryEntity>() {
                override fun areItemsTheSame(oldUser: StoryEntity, newUser: StoryEntity): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: StoryEntity, newUser: StoryEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }

    interface OnItemClicked{
        fun onClicked(story : StoryEntity)
    }
}