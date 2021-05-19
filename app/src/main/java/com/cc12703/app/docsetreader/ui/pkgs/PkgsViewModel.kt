package com.cc12703.app.docsetreader.ui.pkgs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cc12703.app.docsetreader.data.repo.PkgsRepository
import com.cc12703.app.docsetreader.info.PkgInfo
import com.cc12703.app.docsetreader.info.Resource
import com.cc12703.app.docsetreader.util.LOG_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PkgsViewModel @Inject internal constructor(
        private val pkgRepo: PkgsRepository
) : ViewModel() {

    private lateinit var pkgHandler: PkgHandler





    fun getPkg(): LiveData<Resource<List<PkgInfo>>> {
        return pkgRepo.getPkgs()
    }


    fun requestPkgs() {
        pkgRepo.requestPkgs()
    }

    fun refreshPkgs() {
        pkgRepo.refreshPkgs()
    }

    fun showPkg(info: PkgInfo) {
        Log.i(LOG_TAG, "show pkg ${info.name} - ${info.localVer}")
        if(info.isShow()) {
            pkgHandler.onOpenPkg(info)
        }
    }

    fun downloadPkg(info: PkgInfo) {
        Log.i(LOG_TAG, "download pkg ${info.name} - ${info.lastVer}")
        pkgRepo.downloadPkg(info)
    }



    fun setPkgHander(handler: PkgHandler) {
        pkgHandler = handler
    }


}