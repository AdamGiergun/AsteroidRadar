package com.udacity.asteroidradar

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidListAdapter
import com.udacity.asteroidradar.network.NasaApiStatus

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
fun ImageView.bindApod(apod: PictureOfDay?) {
    apod?.let {
        when (it.mediaType) {
            PictureOfDay.MediaType.IMAGE -> {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                Picasso
                    .get()
                    .load(it.url)
                    .into(this)
            }
            PictureOfDay.MediaType.VIDEO -> {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                setImageResource(R.drawable.ic_play_circle)
                contentDescription = context.getString(R.string.touch_to_play_video)
                setOnClickListener { _ ->
                    val videoUri = Uri.parse(it.url)
                    val intent = Intent(Intent.ACTION_VIEW, videoUri)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        startActivity(context, intent, null)
                    }
                }
            }
            else -> {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                setImageResource(R.drawable.ic_broken_image)
            }
        }
        contentDescription = it.title
    }
}

@BindingAdapter("apodDesc")
fun TextView.bindApodDesc(apod: PictureOfDay?) {
    text = if (apod != null && apod.mediaType == PictureOfDay.MediaType.VIDEO)
        context.getString(R.string.video_of_the_day)
    else
        context.getString(R.string.image_of_the_day)
}

@BindingAdapter("apodStatus")
fun ImageView.bindApodStatus(status: NasaApiStatus) {
    when (status) {
        NasaApiStatus.ERROR -> {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            setImageResource(R.drawable.ic_connection_error)
        }
        NasaApiStatus.LOADING -> {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            setImageResource(R.drawable.loading_animation)
        }
        NasaApiStatus.DONE -> {
        }
    }
}

@BindingAdapter("listStatus")
fun CircularProgressIndicator.bindListStatus(status: NasaApiStatus) {
    visibility = when (status) {
        NasaApiStatus.ERROR ->
            View.GONE
        NasaApiStatus.LOADING ->
            View.VISIBLE
        NasaApiStatus.DONE ->
            View.GONE
    }
}

@BindingAdapter("listStatus", "apodStatus")
fun FloatingActionButton.bindListStatus(listStatus: NasaApiStatus, apodStatus: NasaApiStatus) {
    visibility =
        if (listStatus == NasaApiStatus.ERROR || apodStatus == NasaApiStatus.ERROR)
            View.VISIBLE
        else
            View.GONE
}