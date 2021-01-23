package com.githubusers.android.presentation.user.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.githubusers.android.domain.repositories.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


class UserListViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {

    private val searchInput: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val hasConnection: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * LiveData that populate the list of users
     */
    val userList = userRepository.getUsers(30)
        .cachedIn(viewModelScope)


    /**
     * LiveData that populate the list of users based on the searchInput
     */
    @ExperimentalCoroutinesApi
    val searchList = searchInput.asFlow()
        .flatMapLatest { userRepository.searchUsers(it) }
        .cachedIn(viewModelScope)


    fun submitSearch(search: String) {
        if (search.isNotEmpty()) {
            searchInput.value = search
        }
    }
}