
package com.cc12703.app.docsetreader

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.liulishuo.okdownload.core.Util
import dagger.hilt.android.HiltAndroidApp
import org.slf4j.bridge.SLF4JBridgeHandler
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    init {
        Util.enableConsoleLog()
    }


    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(Intent(this, MainService::class.java))
        }
        else {
            this.startService(Intent(this, MainService::class.java))
        }
    }


    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
