package com.example.charactersapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.charactersapp.R
import com.example.charactersapp.databinding.AdapterItemBinding
import com.example.charactersapp.models.Character

class CharactersAdapter(private val onItemClickListener: OnItemClickListener):PagingDataAdapter<Character,CharactersAdapter.Vh>(DiffCallback()) {

    inner class Vh(var adapterItemBinding: AdapterItemBinding):RecyclerView.ViewHolder(adapterItemBinding.root){

        fun bind(character: Character){
            adapterItemBinding.apply {
                Glide.with(itemView)
                    .load(character.image)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.place_holder)
                    .into(image)

                name.text = character.name
                location.text = character.location.name
            }

            adapterItemBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(character)
            }
        }

    }


    private class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(AdapterItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    interface OnItemClickListener {
        fun onItemClick(character: Character)
    }

}