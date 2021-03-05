package com.cc12703.app.docsetreader.ui.pkgs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.cc12703.app.docsetreader.data.PkgLiveData
import com.cc12703.app.docsetreader.data.PkgsRepository

class PkgsViewModel @ViewModelInject internal constructor(
        pkgRepo: PkgsRepository
) : ViewModel() {

    val pkgs: PkgLiveData = pkgRepo.getPkgList()

}