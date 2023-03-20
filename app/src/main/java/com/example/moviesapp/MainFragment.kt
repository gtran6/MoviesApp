package com.example.moviesapp

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.moviesapp.adapter.PopularAdapter
import com.example.moviesapp.adapter.UpcomingAdapter
import com.example.moviesapp.databinding.FragmentMainBinding
import com.example.moviesapp.extra.Events
import com.example.moviesapp.extra.Utils.API_KEY
import com.example.moviesapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.adapter.MovieAdapter
import com.example.moviesapp.adapter.MovieViewHolder
import com.example.moviesapp.model.Result
import com.example.moviesapp.model.ResultX
import com.patrickelm.parallaxdemo.util.HorizontalSpaceItemDecoration
import com.patrickelm.parallaxdemo.util.SlowScrollingLinearLayoutManager
import com.patrickelm.parallaxdemo.util.SlowScrollingPagerSnapHelper

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding

    private var onMovieSelected: OnMovieSelected

    interface OnMovieSelected {
        fun onMovieSelected(bundle: Bundle)
    }

    init {
        onMovieSelected = object : OnMovieSelected {
            override fun onMovieSelected(bundle: Bundle) {
/*                val action = MainFragmentDirections.actionMainFragmentToDetailsFragment()
                findNavController().navigate(action)*/
                val fragment = DetailsFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }
    }

    private val popularAdapter by lazy { PopularAdapter { list ->
        onMovieSelected.onMovieSelected(list)
    } }
    private val upcomingAdapter by lazy { UpcomingAdapter { list ->
        onMovieSelected.onMovieSelected(list)
    } }

    private val movieAdapter by lazy { MovieAdapter { list ->
        onMovieSelected.onMovieSelected(list)
    } }

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
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerTopView()
        setPopularAdapter()
        setUpcomingAdapter()

        mainViewModel.getNowPlayingMovie(API_KEY)
        mainViewModel.nowPlaying.observe( this.viewLifecycleOwner, Observer {
            when (it) {
                is Events.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Events.Success -> {
                    it.let {
                        it.data?.let { it3 ->
                            movieAdapter.differ.submitList(it3)
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is Events.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        mainViewModel.getPopularMovie(API_KEY)
        mainViewModel.popular.observe( this.viewLifecycleOwner, Observer {
            when (it) {
                is Events.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Events.Success -> {
                    it.let {
                        it.data?.let { it2 ->
                            popularAdapter.differ.submitList(it2)
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is Events.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        mainViewModel.getUpcomingMovie(API_KEY)
        mainViewModel.upcoming.observe( this.viewLifecycleOwner, Observer {
            when (it) {
                is Events.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Events.Success -> {
                    it.let {
                        it.data?.let { it3 ->
                            upcomingAdapter.differ.submitList(it3)
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is Events.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun setUpcomingAdapter() = binding.rcvUpcoming.apply {
        adapter = upcomingAdapter
        layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
    }

    private fun setPopularAdapter() = binding.rcvPopular.apply {
        adapter = popularAdapter
        layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
    }

    private fun setRecyclerTopView() = binding.rcvTop.apply {
        adapter = movieAdapter
        layoutManager = SlowScrollingLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL)
        onFlingListener = null
        SlowScrollingPagerSnapHelper(context).attachToRecyclerView(this)
        val space = resources.getDimensionPixelOffset(R.dimen.spacing_card)
        addItemDecoration(HorizontalSpaceItemDecoration(space))

        setupParallax()

        val handler = Handler(Looper.getMainLooper())
        val runnable = object  : Runnable {
            override fun run() {
                val position = (layoutManager as SlowScrollingLinearLayoutManager).findLastVisibleItemPosition() + 1
                if (position < movieAdapter.differ.currentList.size) {
                    smoothScrollToPosition(position)
                } else {
                    smoothScrollToPosition(0)
                }
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    private fun RecyclerView.setupParallax() {
        val viewBounds = Rect()
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = layoutManager as? LinearLayoutManager ?: return
                val scrollOffset = recyclerView.computeHorizontalScrollOffset()
                layoutManager.visiblePositions.forEach { position ->
                    val viewHolder = findViewHolderForAdapterPosition(position) as? MovieViewHolder
                        ?: return@forEach
                    recyclerView.getDecoratedBoundsWithMargins(viewHolder.itemView, viewBounds)
                    val width = viewBounds.width().toFloat()
                    val viewStart = position * width
                    viewHolder.parallaxOffset = (viewStart - scrollOffset) / width
                }
            }
        })
    }

    private val LinearLayoutManager.visiblePositions: IntRange
        get() = (findFirstVisibleItemPosition()..findLastVisibleItemPosition())

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}