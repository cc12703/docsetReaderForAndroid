package com.cc12703.app.docsetreader.data.dl

import android.app.Application
import android.util.Log
import com.cc12703.app.docsetreader.info.PkgInfo
import com.cc12703.app.docsetreader.util.LOG_TAG
import com.cc12703.app.docsetreader.util.PkgNameUtil
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadService @Inject constructor(
    private val application: Application,
) {



    fun downloadPkg(info: PkgInfo, outDir: File, notifyer: DownloadPkgNotifyer) {
        val task = DownloadTask.Builder(info.dlUrl, outDir)
            .setFilename(buildStoreFileName(info))
            .setMinIntervalMillisCallbackProcess(10)
            .setAutoCallbackToUIThread(false)
            .build()

        task.enqueue(DownloadListenerOfPkg(notifyer))
    }


    private fun buildStoreFileName(info: PkgInfo): String {
        val docsetName = PkgNameUtil.build(info.name, info.dlVer)
        return "${docsetName}.tgz"
    }


    private class DownloadListenerOfPkg(
        private val notifyer: DownloadPkgNotifyer,
    ): DownloadListener4WithSpeed() {

        private var totalLength: Long = 0

        override fun taskStart(task: DownloadTask) { }

        override fun connectStart(task: DownloadTask, blockIndex: Int,
                            requestHeaderFields: MutableMap<String, MutableList<String>>) { }

        override fun connectEnd(task: DownloadTask, blockIndex: Int,
            responseCode: Int, responseHeaderFields: MutableMap<String, MutableList<String>>) { }

        override fun progressBlock(task: DownloadTask,
                                   blockIndex: Int, currentBlockOffset: Long, blockSpeed: SpeedCalculator) { }

        override fun blockEnd(task: DownloadTask,
            blockIndex: Int, info: BlockInfo?, blockSpeed: SpeedCalculator) { }

        override fun taskEnd(task: DownloadTask,
            cause: EndCause, realCause: Exception?, taskSpeed: SpeedCalculator) {
            if(cause == EndCause.COMPLETED) {
                Log.d(LOG_TAG, "task ${task.filename} is dl completed")
                notifyer.onSucc(task.file!!)
            }
        }

        override fun infoReady(task: DownloadTask,
            info: BreakpointInfo, fromBreakpoint: Boolean,
            model: Listener4SpeedAssistExtend.Listener4SpeedModel) {
            totalLength = info.totalLength
        }

        override fun progress(task: DownloadTask, currentOffset: Long, taskSpeed: SpeedCalculator) {
            val percent: Int = ((currentOffset.toFloat() / totalLength) * 100).toInt()
            notifyer.onPercent(percent)
        }
    }
}