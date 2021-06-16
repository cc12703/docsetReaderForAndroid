package com.cc12703.app.docsetreader.data.repo

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


val Context.settingsStore by preferencesDataStore("settings")



@Singleton
class SettingRepository @Inject constructor(
    private val app: Application,
) {


    companion object {
        val GITHUB_LOGIN_NAME = stringPreferencesKey("github_login_name")
        val TAG_PREFIX_NAME = stringPreferencesKey("tag_prefix_name")
        val DOCSET_UPDATE_ADDR = stringPreferencesKey("docset_update_addr")
    }





    val githubName = app.settingsStore.data.map { pref -> pref[GITHUB_LOGIN_NAME]?: "" }
    val tagPrefixName = app.settingsStore.data.map { pref -> pref[TAG_PREFIX_NAME]?: "" }
    val docsetUpdateAddr = app.settingsStore.data.map { pref -> pref[DOCSET_UPDATE_ADDR]?: ""}


    suspend fun save(githubLogin: String?, tagPrefix: String?, docsetUpdateAddr: String?) {
        app.settingsStore.edit { pref ->
            if (githubLogin?.isNotEmpty()!!) {
                pref[GITHUB_LOGIN_NAME] = githubLogin
            }
            if (tagPrefix?.isNotEmpty()!!) {
                pref[TAG_PREFIX_NAME] = tagPrefix
            }
            if (docsetUpdateAddr?.isNotEmpty()!!) {
                pref[DOCSET_UPDATE_ADDR] = docsetUpdateAddr
            }
        }
    }
}