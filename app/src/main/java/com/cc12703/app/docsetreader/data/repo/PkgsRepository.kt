package com.cc12703.app.docsetreader.data.repo

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.cc12703.app.docsetreader.data.db.PkgDao
import com.cc12703.app.docsetreader.data.dl.DownloadPkgNotifyer
import com.cc12703.app.docsetreader.data.dl.DownloadService
import com.cc12703.app.docsetreader.info.PkgInfo
import com.cc12703.app.docsetreader.info.PkgRemoteInfo
import com.cc12703.app.docsetreader.info.Resource
import com.cc12703.app.docsetreader.info.Status
import com.cc12703.app.docsetreader.util.CompressUtil
import com.cc12703.app.docsetreader.util.LOG_TAG
import com.cc12703.app.docsetreader.util.PathUtil
import com.cc12703.app.docsetreader.util.PkgNameUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PkgsRepository @Inject constructor(
    private val application: Application,
    private val pkgDao: PkgDao,
    private val remote: PkgsRemoteRepository,
    private val pkgDL: DownloadService,
) {

    private val pkgs = MediatorLiveData<Resource<List<PkgInfo>>>()
    private var dbSource: LiveData<List<PkgInfo>>? = null
    private var remoteSource: LiveData<Resource<List<PkgRemoteInfo>>>? = null


    fun getPkgs(): LiveData<Resource<List<PkgInfo>>> {
        return pkgs
    }


    fun refreshPkgs() {
        if(dbSource != null) {
            pkgs.removeSource(dbSource!!)
            dbSource = null
        }
        if(remoteSource != null) {
            pkgs.removeSource(remoteSource!!)
            remoteSource = null
        }
        requestPkgs(true)
    }


    fun requestPkgs(isForceFetch: Boolean = false) {

        pkgs.value = Resource.loading(null)

        val dbSrc = pkgDao.getAll()
        dbSource = dbSrc
        pkgs.addSource(dbSrc) { data ->
            pkgs.removeSource(dbSrc)
            if(isForceFetch || data==null || data.isEmpty()) {
                remoteSource = fetchFromRemote(pkgs, dbSrc)
            }
            else {
                pkgs.addSource(dbSrc) { pkgs.value = Resource.success(it) }
            }
        }
    }

    private fun fetchFromRemote(result: MediatorLiveData<Resource<List<PkgInfo>>>,
                                dbSrc: LiveData<List<PkgInfo>>): LiveData<Resource<List<PkgRemoteInfo>>> {
        val remoteSrc = remote.getPkgs()
        result.addSource(remoteSrc) { res ->
            result.removeSource(remoteSrc)

            when(res.status) {
                 Status.SUCCESS -> {
                     GlobalScope.launch(Dispatchers.IO) {
                         if(res.data!!.isNotEmpty()) {
                             pkgDao.insertPkgs(buildPkgInfos(dbSrc.value, res.data!!))
                         }
                         GlobalScope.launch(Dispatchers.Main){
                             result.addSource(pkgDao.getAll()) { result.value = Resource.success(it) }
                         }
                     }
                 }
                Status.ERROR -> {
                    result.addSource(pkgDao.getAll()) {
                        result.value = if (it.isEmpty()) Resource.error(res.message!!, null)
                                        else Resource.success(it)
                    }
                }
            }




        }
        return remoteSrc
    }

    private fun buildPkgInfos(dbInfos: List<PkgInfo>?, remoteInfos: List<PkgRemoteInfo>): List<PkgInfo> {
        return remoteInfos.map { rInfo ->
            val dInfo = dbInfos?.firstOrNull { it.name == rInfo.name }
            if(dInfo == null) {
                PkgInfo.create(rInfo)
            }
            else {
                dInfo.lastVer = rInfo.ver
                dInfo.lastUrl = rInfo.url
                dInfo
            }
        }
    }

    fun downloadPkg(info: PkgInfo) {
        Log.i(LOG_TAG, "download ${info.name}")
        GlobalScope.launch(Dispatchers.IO) {
            info.dlVer = info.lastVer
            info.dlUrl = info.lastUrl
            info.dlPercent = 0
            pkgDao.updatePkg(info)

            pkgDL.downloadPkg(info, PathUtil.buildPkgTempPath(application), object: DownloadPkgNotifyer {
                override fun onSucc(file: File) {
                    Log.i(LOG_TAG, "docset tgz ${file}")

                    uncompressDocsetPkg(file, info)

                    info.localVer = info.dlVer
                    info.dlVer = ""
                    pkgDao.updatePkg(info)
                }

                override fun onPercent(value: Int) {
                    info.dlPercent = value
                    pkgDao.updatePkg(info)
                }


            })
        }
    }


    private fun uncompressDocsetPkg(src: File, info: PkgInfo) {
        CompressUtil.uncompressTarGzipFile(src, PathUtil.buildPkgStorePath(application))
        val oldPath = File(PathUtil.buildPkgStorePath(application), "${info.name}.${PkgNameUtil.SUFFIX_NAME}")
        val newPath = File(PathUtil.buildPkgStorePath(application), PkgNameUtil.build(info.name, info.dlVer))
        oldPath.renameTo(newPath)
    }


}