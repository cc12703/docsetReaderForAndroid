package com.cc12703.app.docsetreader.data

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingRepository @Inject constructor(
    private val application: Application,
) {

    companion object {
        val GITHUB_LOGIN_NAME = stringPreferencesKey("github_login_name")
        val TAG_PREFIX_NAME = stringPreferencesKey("tag_prefix_name")
    }


    private val store = application.createDataStore(name="settings")


    val githubName = store.data.map { pref -> pref[GITHUB_LOGIN_NAME]?: "" }
    val tagPrefixName = store.data.map { pref -> pref[TAG_PREFIX_NAME]?: "" }


    suspend fun save(githubLogin: String?, tagPrefix: String?) {
        store.edit { pref ->
            if (githubLogin?.isNotEmpty()!!) {
                pref[GITHUB_LOGIN_NAME] = githubLogin
            }
            if (tagPrefix?.isNotEmpty()!!) {
                pref[TAG_PREFIX_NAME] = tagPrefix
            }
        }
    }
}