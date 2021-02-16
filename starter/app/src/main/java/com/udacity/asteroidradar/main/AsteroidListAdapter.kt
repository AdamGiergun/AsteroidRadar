package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

class AsteroidListAdapter(private val clickListener: AsteroidClickListener): ListAdapter<Asteroid, AsteroidListAdapter.AsteroidViewHolder>(DiffCallback) {

    class AsteroidViewHolder(private var binding: AsteroidListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: AsteroidClickListener, _asteroid: Asteroid) {
            binding.run {
                asteroid = _asteroid
                clickListener = listener
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidListItemBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Asteroid>()  {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(clickListener, asteroid)
    }
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}
