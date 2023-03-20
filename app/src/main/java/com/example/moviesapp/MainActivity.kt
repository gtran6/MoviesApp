package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.moviesapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(this, navController)
        Log.d("main-activity", "working")

        setUpDrawerLayout()
    }

    private fun setUpDrawerLayout() {
        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(this, navController, binding.drawer)
    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp() || super.onSupportNavigateUp()
        return NavigationUI.navigateUp(navController, binding.drawer)
    }

    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
        /*setPopularAdapter()
        setUpcomingAdapter()

        mainViewModel.getNowPlayingMovie(API_KEY)
        mainViewModel.nowPlaying.observe(this, Observer {
            when (it) {
                is Events.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Events.Success -> {
                    it.let {
                        it.data?.let { it1 ->
                            setRecyclerTopView(it1)
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
        mainViewModel.popular.observe(this, Observer {
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
        mainViewModel.upcoming.observe(this, Observer {
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
        layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
    }

    private fun setPopularAdapter() = binding.rcvPopular.apply {
        adapter = popularAdapter
        layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
    }

    private fun setRecyclerTopView(list: List<Result>) = binding.rcvTop.apply {
        adapter = MovieAdapter(list)
        layoutManager = SlowScrollingLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL)
        SlowScrollingPagerSnapHelper(context).attachToRecyclerView(this)
        val space = resources.getDimensionPixelOffset(R.dimen.spacing_card)
        addItemDecoration(HorizontalSpaceItemDecoration(space))

        setupParallax()

        val handler = Handler(Looper.getMainLooper())
        val runnable = object  : Runnable {
            override fun run() {
                val position = (layoutManager as SlowScrollingLinearLayoutManager).findLastVisibleItemPosition() + 1
                if (position < list.size) {
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
                    val viewHolder = findViewHolderForAdapterPosition(position) as? MovieViewHolder ?: return@forEach
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
    }
}*/