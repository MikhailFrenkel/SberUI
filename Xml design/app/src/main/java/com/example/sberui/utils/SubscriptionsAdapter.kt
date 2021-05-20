package com.example.sberui.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sberui.databinding.ItemSubscriptionBinding

class SubscriptionsAdapter : ListAdapter<SubscriptionItem,
        RecyclerView.ViewHolder>(SubscriptionsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubscriptionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SubscriptionViewHolder) {
            holder.bind(getItem(position))
        }
    }

    class SubscriptionViewHolder private constructor(
        private val binding:  ItemSubscriptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SubscriptionItem) {
            binding.item = item
            binding.img.setImageResource(item.imgRes)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : SubscriptionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSubscriptionBinding.inflate(layoutInflater, parent, false)

                return SubscriptionViewHolder(binding)
            }
        }
    }
}

class SubscriptionsDiffCallback : DiffUtil.ItemCallback<SubscriptionItem>() {
    override fun areItemsTheSame(oldItem: SubscriptionItem, newItem: SubscriptionItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SubscriptionItem, newItem: SubscriptionItem): Boolean {
        return oldItem == newItem
    }

}

data class SubscriptionItem(
    val id: Int,
    val title: String,
    val imgRes: Int,
    val text: String,
    val subText: String
)