package com.dicoding.restaurantreview.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.databinding.FragmentFollowBinding
import com.dicoding.restaurantreview.ui.main.ReviewAdapter


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: FollowFragmentViewModel
    private lateinit var usersAdapter: ReviewAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FollowFragmentViewModel::class.java]

        usersAdapter = ReviewAdapter(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem, position: Int) {
                Toast.makeText(requireContext(), "Clicked item: ${data.login}", Toast.LENGTH_SHORT).show()
            }
        })

        username = arguments?.getString(ARG_USERNAME) ?: ""

        val position = requireArguments().getInt(ARG_POSITION)

        if (position == 1) {
            showLoading(true)
            viewModel.fetchFollowers(username)
        } else {
            showLoading(true)
            viewModel.fetchFollowing(username)
        }

        viewModel.followers.observe(viewLifecycleOwner) { followers ->
            usersAdapter.submitList(followers)
            showLoading(false)
        }

        viewModel.following.observe(viewLifecycleOwner) { following ->
            usersAdapter.submitList(following)
            showLoading(false)
        }

        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.adapter = usersAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}

