package com.udacity.asteroidradar

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
                Picasso.get().load(apod.url).into(this)
            }
            AstronomyPictureOfTheDay.MediaType.VIDEO -> {
                setImageResource(android.R.drawable.ic_media_play)
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            else -> {
                setImageResource(R.drawable.placeholder_picture_of_day)
            }
        }
    }
}