package com.example.sberui.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sberui.databinding.ItemLimitBinding

class LimitsAdapter : ListAdapter<LimitItem,
        RecyclerView.ViewHolder>(LimitsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LimitItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LimitItemViewHolder) {
            holder.bind(getItem(position), itemCount - 1 == position)
        }
    }

    class LimitItemViewHolder private constructor(
        private val binding: ItemLimitBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LimitItem, isLast: Boolean) {
            binding.item = item
            binding.img.setImageResource(item.imgRes)
            binding.divider.visibility = if (isLast) View.GONE else View.VISIBLE
            binding.subheader.visibility = if (item.subHeader == "") View.GONE else View.VISIBLE
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : LimitItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLimitBinding.inflate(layoutInflater, parent, false)
                return LimitItemViewHolder(binding)
            }
        }
    }
}

class LimitsDiffCallback : DiffUtil.ItemCallback<LimitItem>() {
    override fun areItemsTheSame(oldItem: LimitItem, newItem: LimitItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LimitItem, newItem: LimitItem): Boolean {
        return oldItem == newItem
    }

}

data class LimitItem(
    val id: Int,
    val imgRes: Int,
    val header: String,
    val subHeader: String
)