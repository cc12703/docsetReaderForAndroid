package com.cc12703.app.docsetreader.ui.setting

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cc12703.app.docsetreader.data.SettingRepository
import com.cc12703.app.docsetreader.util.LOG_TAG
import kotlinx.coroutines.launch

class SettingViewModel @ViewModelInject internal constructor(
    private val repo: SettingRepository
) : ViewModel()  {


    val gitHubName = repo.githubName.asLiveData() as MutableLiveData

    val tagPrefixName = repo.tagPrefixName.asLiveData() as MutableLiveData


    fun save() {
        viewModelScope.launch {
            Log.i(LOG_TAG, "save githubname ${gitHubName.value}")
            Log.i(LOG_TAG, "save tagprefixname ${tagPrefixName.value}")
            repo.save(gitHubName.value, tagPrefixName.value)
        }
    }
}