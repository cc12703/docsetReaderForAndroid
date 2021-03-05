package com.cc12703.app.docsetreader.data

import android.os.FileObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cc12703.app.docsetreader.info.PkgInfo
import java.io.File
import java.io.FileFilter


private class MyFileObserver(val liveData: MutableLiveData<MutableList<PkgInfo>>, path: File)
                            : FileObserver(path.absolutePath, CREATE or DELETE) {
    override fun onEvent(event: Int, path: String?) {
        val realEvent = event and ALL_EVENTS
        if(realEvent == CREATE) {
            val pathFile = File(path)
            val data = liveData.value
            data?.add(PkgInfo(pathFile.name, pathFile))
            liveData.postValue(data)
        }
        else if(realEvent == DELETE) {
            val data = liveData.value
            data?.removeIf { pInfo -> pInfo.path.absolutePath == path }
            liveData.postValue(data)
        }
    }

}

class PkgLiveData(
        private val rootPath: File
) : MutableLiveData<MutableList<PkgInfo>>(mutableListOf()) {

    private val observer: FileObserver by lazy {
        MyFileObserver(this, rootPath)
    }


    override fun onActive() {
        super.onActive()
        scanOnce()
        observer.startWatching()
    }

    override fun onInactive() {
        super.onInactive()
        observer.stopWatching()
    }

    private fun scanOnce() {
        val files = rootPath.listFiles()
        if (files == null)
            return

        val pkgs = files.asSequence()
                .filter { it.name.endsWith("docset") }
                .map { PkgInfo(it.name, it) }
                .toList()
        val data = value
        data?.addAll(pkgs)
        setValue(data)
    }

}