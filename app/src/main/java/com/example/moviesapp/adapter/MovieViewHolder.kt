package com.example.moviesapp.adapter

import android.animation.FloatEvaluator
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.core.view.doOnPreDraw
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviesapp.DetailsFragment
import com.example.moviesapp.databinding.ItemSliderBinding
import kotlin.math.abs
import com.example.moviesapp.model.Result

// how much of the image to scroll
private const val IMAGE_PARALLAX_FACTOR = 1f / 3f
// how much the image should fade in and out
private const val IMAGE_MIN_ALPHA = 0.80f
private const val IMAGE_MAX_ALPHA = 1.0f

// how the text should move, coming from the left
private const val TITLE_LEFT_PARALLAX_FACTOR = 0.35f
private const val MESSAGE_LEFT_PARALLAX_FACTOR = 0.2f
private const val EXTRA_LEFT_PARALLAX_FACTOR = 0.35f

// how the text should move, coming from the right
private const val TITLE_RIGHT_PARALLAX_FACTOR = 0.3f
private const val MESSAGE_RIGHT_PARALLAX_FACTOR = 1.5f
private const val EXTRA_RIGHT_PARALLAX_FACTOR = 1.0f

class MovieViewHolder(private val binding: ItemSliderBinding, private val onMovieSelected: (Bundle) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    private val linearEvaluator = FloatEvaluator()
    private val linearToSlowInterpolator = LinearOutSlowInInterpolator()

    var parallaxOffset: Float = 0f
        set(value) {
            // Make sure that the value is limited to the range of [-1,1]
            field = value.coerceIn(-1f, 1f)
            // Adjust the parallax based on the new value
            applyParallax(field)
        }

    fun bind(model: Result) = binding.apply {
        image.load("https://image.tmdb.org/t/p/original/"+model.poster_path)
        image.doOnPreDraw { img ->
            val parent = (itemView.parent as? View)?.width ?: 0
            val width = img.width.toFloat()
            val scale = (parent - (1f - IMAGE_PARALLAX_FACTOR) * (parent - width) / 2f) / width
            img.scaleX = scale
            img.scaleY = scale
        }
        title.text = model.title
        description.text = model.original_title
        extra.text = model.release_date

        root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("poster_path", model.poster_path)
            bundle.putString("title", model.original_title)
            bundle.putString("release_date",model.release_date)
            bundle.putString("original_language", model.original_language)
            bundle.putString("popularity", model.popularity.toString())
            bundle.putString("vote_average", model.vote_average.toString())
            bundle.putString("overview", model.overview)
            onMovieSelected(bundle)
        }
    }

    private fun applyParallax(offset: Float) = binding.apply {
        val direction = if (offset < 0f) -1f else 1f
        val absoluteValue = abs(offset)
        val width = card.width

        // Add separate parallax effects for each text field and for each direction
        if (direction == -1f) { // moving left
            val fraction = -linearToSlowInterpolator.getInterpolation(absoluteValue)
            title.translationX = fraction * width * TITLE_LEFT_PARALLAX_FACTOR
            description.translationX = fraction * width * MESSAGE_LEFT_PARALLAX_FACTOR
            extra.translationX = fraction * width * EXTRA_LEFT_PARALLAX_FACTOR
        } else { // moving right
            // Invert the interpolator to have the translation slow down when
            // approaching offset = 0
            val fraction = 1f - linearToSlowInterpolator.getInterpolation(1f - absoluteValue)
            title.translationX = fraction * width * TITLE_RIGHT_PARALLAX_FACTOR
            description.translationX = fraction * width * MESSAGE_RIGHT_PARALLAX_FACTOR
            extra.translationX = fraction * width * EXTRA_RIGHT_PARALLAX_FACTOR
        }

        //https://image.tmdb.org/t/p/original/r7Dfg9aRZ78gJsmDlCirIIlNH3d.jpg
        image.translationX = -(absoluteValue * direction * width * IMAGE_PARALLAX_FACTOR)
        image.alpha = linearEvaluator.evaluate(absoluteValue, IMAGE_MAX_ALPHA, IMAGE_MIN_ALPHA)
    }
}