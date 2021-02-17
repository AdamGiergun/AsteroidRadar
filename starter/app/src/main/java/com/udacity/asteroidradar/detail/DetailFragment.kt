package com.udacity.asteroidradar.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailBinding.inflate(inflater).run {
            lifecycleOwner = this@DetailFragment
            asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid
            auExplanationButton.setOnClickListener { displayAstronomicalUnitExplanationDialog() }
            root
        }
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        AlertDialog.Builder(requireActivity()).run {
            setMessage(getString(R.string.astronomical_unit_explanation))
            setPositiveButton(android.R.string.ok, null)
            create()
            show()
        }
    }
}