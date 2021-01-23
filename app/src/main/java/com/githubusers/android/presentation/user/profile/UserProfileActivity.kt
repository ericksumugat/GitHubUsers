package com.githubusers.android.presentation.user.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import com.githubusers.android.BR
import com.githubusers.android.R
import com.githubusers.android.databinding.ActivityProfileBinding
import com.githubusers.android.presentation.BaseActivity
import com.githubusers.android.presentation.utils.NetworkStatus
import javax.inject.Inject

class UserProfileActivity : BaseActivity() {

    companion object {

        private const val USER_NAME_KEY = "userName"

        fun createIntent(
            context: Context,
            userName: String,
        ): Intent {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(USER_NAME_KEY, userName)
            return intent
        }
    }

    @Inject
    lateinit var viewModel: UserProfileViewModel

    private var binding: ActivityProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performBinding()
        setNoteListener()

        viewModel.showUserProfile(intent.getStringExtra(USER_NAME_KEY)!!)
    }

    private fun performBinding() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding?.setVariable(BR.viewModel, viewModel)
        binding?.executePendingBindings()

        binding?.lifecycleOwner = this
    }

    private fun setNoteListener(){
        binding?.etNote?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val note = binding?.etNote?.text.toString() ?: ""
                viewModel.submitNote(note)
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onNetworkConnectionChange(networkStatus: NetworkStatus) {
        viewModel.hasConnection.postValue(networkStatus == NetworkStatus.CONNECTED)
    }
}