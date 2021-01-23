package com.githubusers.android.presentation.user.list

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.githubusers.android.BR
import com.githubusers.android.R
import com.githubusers.android.data.exceptions.network.NoNetworkException
import com.githubusers.android.data.sources.local.UserEntity
import com.githubusers.android.databinding.ActivityUserListBinding
import com.githubusers.android.presentation.BaseActivity
import com.githubusers.android.presentation.user.profile.UserProfileActivity
import com.githubusers.android.presentation.utils.NetworkStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserListActivity : BaseActivity(), UserListAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModel: UserListViewModel
    private val adapter: UserListAdapter = UserListAdapter(this)
    private var binding: ActivityUserListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performBinding()

        initSwipeToRefresh()
        setListAdapter()
    }

    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.default_menu, menu)

        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        initSearchView(searchView)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemClick(user: UserEntity) {
        startActivity(UserProfileActivity.createIntent(this, user.login))
    }

    @ExperimentalCoroutinesApi
    private fun initSearchView(searchView: SearchView) {
        searchView.setOnCloseListener {
            lifecycleScope.launch { onSearchInactive() }
            return@setOnCloseListener false
        }
        searchView.setOnSearchClickListener {
            lifecycleScope.launch { onSearchActive() }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.submitSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.submitSearch(it)
                }
                return true
            }
        })
    }

    private fun performBinding() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_user_list)
        binding?.setVariable(BR.viewModel, viewModel)
        binding?.executePendingBindings()

        binding?.lifecycleOwner = this
    }

    private fun setListAdapter() {
        binding?.rvUserList?.adapter = adapter.withLoadStateFooter(footer = PostsLoadStateAdapter())

        lifecycleScope.launchWhenCreated {
            viewModel.userList.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding?.swipeRefresh?.isRefreshing = loadState.refresh is LoadState.Loading
                listErrorHandling(loadState)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun onSearchActive() {
        viewModel.searchList.collectLatest {
            adapter.submitData(it)
        }
    }

    private suspend fun onSearchInactive() {
        viewModel.userList.collectLatest {
            adapter.submitData(it)
        }
    }

    private fun initSwipeToRefresh() {
        binding?.swipeRefresh?.setOnRefreshListener { adapter.refresh() }
    }

    private fun listErrorHandling(loadState: CombinedLoadStates) {
        val error = when {
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            else -> null
        }

        error?.let {
            val errorMsg = when (it.error) {
                is NoNetworkException -> {
                    viewModel.hasConnection.postValue(false)
                    getString(R.string.no_connection)
                }
                else -> getString(R.string.general_error)
            }

            Toast.makeText(this@UserListActivity, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onNetworkConnectionChange(networkStatus: NetworkStatus) {
        viewModel.hasConnection.postValue(networkStatus == NetworkStatus.CONNECTED)

        if (networkStatus == NetworkStatus.CONNECTED && adapter.itemCount == 0){
            adapter.retry()
        }
    }
}