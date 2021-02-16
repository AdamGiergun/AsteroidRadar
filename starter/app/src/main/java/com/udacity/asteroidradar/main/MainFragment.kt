package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater).run {
            lifecycleOwner = this@MainFragment
            viewModel = mainViewModel
            asteroidRecycler.adapter =
                AsteroidListAdapter(AsteroidClickListener { asteroid ->
                    findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                })
            refreshButton.setOnClickListener { mainViewModel.refreshDataIfError() }
            setHasOptionsMenu(true)
            root
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainViewModel.setFilters(item.itemId)
        return true
    }
}