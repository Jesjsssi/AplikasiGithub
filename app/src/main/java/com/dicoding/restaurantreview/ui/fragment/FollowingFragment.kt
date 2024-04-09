package com.dicoding.restaurantreview.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.databinding.FragmentFollowBinding
import com.dicoding.restaurantreview.data.retrofit.ApiConfig
import com.dicoding.restaurantreview.ui.main.ReviewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var username: String
    private lateinit var usersAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        val index = arguments?.getInt(ARG_POSITION, 0)
        username = arguments?.getString(ARG_USERNAME).toString()

        usersAdapter = ReviewAdapter(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem, position: Int) {
                // Handle item click event here
            }
        })

        binding.rvReview.adapter = usersAdapter

        if (index == 1) {
            // Fetch following
            fetchFollowing(username)
        }
    }

    private fun fetchFollowing(username: String) {
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    val followingList = response.body()
                    followingList?.let {
                        setGithubData(it)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGithubData(item: List<ItemsItem>) {
        usersAdapter.submitList(item)
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
        const val TAG = "FollowingFragment"
    }
}