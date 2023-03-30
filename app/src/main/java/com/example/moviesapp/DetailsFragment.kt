package com.example.moviesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviesapp.databinding.FragmentDetailsBinding
import com.example.moviesapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentDetailsBinding
    private val mainViewModel: MainViewModel by viewModels()
    //private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val posterPath = arguments?.getString("poster_path")
        posterPath?.let {
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/original/$posterPath")
                .into(binding.image)
        }

        val title = arguments?.getString("title")
        val release_date = arguments?.getString("release_date")
        val original_language = arguments?.getString("original_language")
        val popularity = arguments?.getString("popularity")
        val vote_average = arguments?.getString("vote_average")
        val overview = arguments?.getString("overview")

        binding.title.text = title
        binding.releaseDate.text = release_date
        binding.originalLanguage.text = original_language
        binding.popularity.text = popularity
        binding.voteAverage.text = vote_average
        binding.overview.text = overview
/*        binding.backButton.setOnClickListener {
            MainActivity().supportFragmentManager.popBackStack()
        }*/
/*
        val movie = args.movieData
        binding.heartButton.setOnClickListener {
            mainViewModel.saveMovie(movie)
            //Snackbar.make(binding.root, "Movie saved", Snackbar.LENGTH_LONG).show()*/
    }
}