package com.cc12703.app.docsetreader.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.cc12703.app.docsetreader.api.*
import com.cc12703.app.docsetreader.api.info.GithubRelease
import com.cc12703.app.docsetreader.info.PkgInfo
import com.cc12703.app.docsetreader.info.PkgRemoteInfo
import com.cc12703.app.docsetreader.info.Resource
import com.cc12703.app.docsetreader.util.LOG_TAG
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PkgsRemoteRepository @Inject constructor(
        private val service: GithubService,
) {


    fun getPkgs(): LiveData<Resource<List<PkgRemoteInfo>>> {
        return Transformations.switchMap(service.getRepos("cc12703")) { resp ->
            liveData(context = Dispatchers.IO) {
                if (resp is ApiEmptyResponse) {
                    emit(Resource.success(emptyList<PkgRemoteInfo>()))
                }
                else if (resp is ApiFailResponse) {
                    emit(Resource.error(resp.message, null))
                }
                else if (resp is ApiSuccessResponse) {
                    Log.i(LOG_TAG, "remote repo num ${resp.body.size}")
                    val result = mutableListOf<PkgRemoteInfo>()
                    for (repo in resp.body) {
                        if(repo.isFork)
                            continue
                        if(!repo.name.startsWith("learning_note"))
                            continue

                        val rel = getLastRelease(repo.name)
                        if(rel?.name == null)
                            continue
                        if(rel.assets==null || rel.assets.isEmpty())
                            continue
                        Log.i(LOG_TAG, "find repo ${repo.name} -- ${rel.tag}")
                        result.add(PkgRemoteInfo(repo.name, rel.tag, rel.assets[0].downloadUrl))
                    }
                    emit(Resource.success(result))
                }
            }
        }
    }

    private fun getLastRelease(repoName: String): GithubRelease? {
        val rawResp = service.getLatestRel("cc12703", repoName).execute()
        val resp = ApiResponse.create(rawResp)
        return if (resp !is ApiSuccessResponse) null else resp.body
    }

}