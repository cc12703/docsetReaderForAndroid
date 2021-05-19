package com.cc12703.app.docsetreader.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cc12703.app.docsetreader.data.api.*
import com.cc12703.app.docsetreader.data.api.info.GithubRelease
import com.cc12703.app.docsetreader.info.PkgRemoteInfo
import com.cc12703.app.docsetreader.info.Resource
import com.cc12703.app.docsetreader.util.LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PkgsRemoteRepository @Inject constructor(
    private val service: GithubService,
    private val settingRepo: SettingRepository,
) {


    fun getPkgs(): LiveData<Resource<List<PkgRemoteInfo>>> {
        return liveData(context = Dispatchers.IO) {
                val login = settingRepo.githubName.first()
                val prefix = settingRepo.tagPrefixName.first()

                Log.i(LOG_TAG, "setting info ${login} - ${prefix}")
                if(login.isEmpty()) {
                    emit(Resource.error("cant setting info", null))
                }

                val resp = service.getRepos(login)

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
                        if(!repo.name.startsWith(prefix))
                            continue

                        val rel = getLastRelease(repo.owner.login, repo.name)
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

    private fun getLastRelease(owner: String, repoName: String): GithubRelease? {
        val resp = service.getLatestRel(owner, repoName)
        return if (resp !is ApiSuccessResponse) null else resp.body
    }

}