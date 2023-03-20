package com.example.moviesapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.databinding.ItemMovieBinding
import com.example.moviesapp.model.ResultX

class PopularAdapter(private val onMovieSelected: (Bundle) -> Unit) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ResultX>() {
        override fun areItemsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    class PopularViewHolder(var binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load("https://image.tmdb.org/t/p/original/" + differ.currentList[position].poster_path)
                .into(holder.binding.image)
            root.setOnClickListener {
                //onMovieSelected(differ.currentList[position])
                val bundle = Bundle()
                bundle.putString("poster_path", differ.currentList[position].poster_path)
                bundle.putString("title", differ.currentList[position].original_title)
                bundle.putString("release_date", differ.currentList[position].release_date)
                bundle.putString("original_language", differ.currentList[position].original_language)
                bundle.putString("popularity", differ.currentList[position].popularity.toString())
                bundle.putString("vote_average", differ.currentList[position].vote_average.toString())
                bundle.putString("overview", differ.currentList[position].overview)
                onMovieSelected(bundle)
            }
        }
    }
}