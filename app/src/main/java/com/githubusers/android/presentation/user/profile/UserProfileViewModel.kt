package com.githubusers.android.presentation.user.profile

import androidx.lifecycle.*
import com.githubusers.android.data.sources.local.UserEntity
import com.githubusers.android.domain.repositories.UserRepository
import com.githubusers.android.presentation.utils.NetworkStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val userNameLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val hasConnection: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * Used for tracking the latest User result from userLiveData
     */
    private var latestUserResult: UserResult? = null

    /**
     * User name of the GitHub user
     */
    private var userName: String = ""


    /**
     * userLiveData is binded to the view and whenever userNameLiveData emits a value it will retrieve the user detail.
     *
     * userRepository.getUser(userName) will emit two one, first will come from DB and the next is from services.
     */
    var userLiveData = Transformations.map(userNameLiveData) { userName ->
        // Converts RxJava Flowable to LiveData
        LiveDataReactiveStreams.fromPublisher(userRepository.getUser(userName)
            .map { UserResult(user = it) }
            .onErrorReturn { UserResult(user = latestUserResult?.user, throwable = it) }
            .doOnNext {
                latestUserResult = it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toFlowable(BackpressureStrategy.LATEST))
    }

    fun showUserProfile(userName: String) {
        this.userName = userName
        userNameLiveData.value = userName
    }

    fun submitNote(note: String) {
        val result = userLiveData.value?.value
        result?.user?.let {
            it.note = note
            userRepository.updateUser(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    init {
        viewModelScope.launch {
            hasConnection.asFlow().collectLatest {
                // when connected retry the retrieval of user data if the request previously failed.
                if (it) {
                    retryIfLatestRequestFailed()
                }
            }
        }
    }

    private fun retryIfLatestRequestFailed(){
        latestUserResult?.throwable?.let {
            userNameLiveData.value = userName
        }
    }
}

class UserResult(val user: UserEntity? = null, val throwable: Throwable? = null)