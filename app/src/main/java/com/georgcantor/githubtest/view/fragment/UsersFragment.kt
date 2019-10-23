package com.georgcantor.githubtest.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.georgcantor.githubtest.R
import com.georgcantor.githubtest.model.data.UserEntry
import com.georgcantor.githubtest.utils.DisposableManager
import com.georgcantor.githubtest.utils.EndlessRecyclerViewScrollListener
import com.georgcantor.githubtest.view.adapter.UsersAdapter
import com.georgcantor.githubtest.viewmodel.UsersViewModel
import kotlinx.android.synthetic.main.fragment_users.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class UsersFragment : Fragment() {

    private lateinit var adapter: UsersAdapter
    private lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_users, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = StaggeredGridLayoutManager(
            3,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = gridLayoutManager
        adapter = UsersAdapter(requireContext())
        recyclerView.adapter = adapter

        val scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadUsers(page)
            }
        }
        scrollListener.resetState()
        recyclerView.addOnScrollListener(scrollListener)

        refreshLayout.setOnRefreshListener {
            loadUsers(1)
            refreshLayout.isRefreshing = false
        }

        loadUsers(1)
    }

    fun loadUsers(page: Int) {
        val disposable = viewModel.getUsers("georg", page, 30)
            .retry(3)
            .subscribe({
                adapter.setUsers(it as MutableList<UserEntry>)
            }, {
            })

        DisposableManager.add(disposable)
    }

    override fun onDestroy() {
        DisposableManager.dispose()
        super.onDestroy()
    }

}