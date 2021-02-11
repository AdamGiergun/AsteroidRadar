package com.udacity.asteroidradar

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidListAdapter
import com.udacity.asteroidradar.network.AstronomyPictureOfTheDay

@BindingAdapter("listData")
fun RecyclerView.bindData(data: List<Asteroid>?) {
    (adapter as AsteroidListAdapter).submitList(data)
}

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    contentDescription = if (isHazardous) {
        setImageResource(R.drawable.ic_status_potentially_hazardous)
        context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        setImageResource(R.drawable.ic_status_normal)
        context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    contentDescription = if (isHazardous) {
        setImageResource(R.drawable.asteroid_hazardous)
        context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        setImageResource(R.drawable.asteroid_safe)
        context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindAstronomicalUnit(number: Double) {
    text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun TextView.bindKmUnit(number: Double) {
    text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun TextView.bindDisplayVelocity(number: Double) {
    text = String.format(context.getString(R.string.km_s_unit_format), number)
}


@BindingAdapter("apod")
fun ImageView.bindApod(apod: AstronomyPictureOfTheDay?) {
    apod?.let {
        when (apod.mediaType) {
            AstronomyPictureOfTheDay.MediaType.IMAGE -> {
                Picasso
                    .get()
                    .load(apod.url)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .into(this)
            }
            AstronomyPictureOfTheDay.MediaType.VIDEO -> {
                setImageResource(R.drawable.ic_play_circle)
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                contentDescription = context.getString(R.string.touch_to_play_video)
                setOnClickListener {
                    val videoUrl: Uri = Uri.parse(apod.url)
                    val intent = Intent(Intent.ACTION_VIEW, videoUrl)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        startActivity(context, intent, null)
                    }
                }
            }
            else -> {
                setImageResource(R.drawable.ic_broken_image)
            }
        }
    }
}