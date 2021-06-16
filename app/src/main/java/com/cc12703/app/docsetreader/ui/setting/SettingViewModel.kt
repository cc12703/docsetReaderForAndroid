package com.cc12703.app.docsetreader.ui.setting

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cc12703.app.docsetreader.data.repo.SettingRepository
import com.cc12703.app.docsetreader.util.LOG_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject internal constructor(
    private val repo: SettingRepository
) : ViewModel()  {


    val gitHubName = repo.githubName.asLiveData() as MutableLiveData

    val tagPrefixName = repo.tagPrefixName.asLiveData() as MutableLiveData

    val docsetUpdateAddr = repo.docsetUpdateAddr.asLiveData() as MutableLiveData


    fun save() {
        viewModelScope.launch {
            Log.i(LOG_TAG, "save githubname ${gitHubName.value}")
            Log.i(LOG_TAG, "save tagprefixname ${tagPrefixName.value}")
            Log.i(LOG_TAG, "save docsetupdateaddr ${docsetUpdateAddr.value}")
            repo.save(gitHubName.value, tagPrefixName.value, docsetUpdateAddr.value)
        }
    }
}