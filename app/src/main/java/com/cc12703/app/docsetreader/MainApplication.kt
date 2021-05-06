
package com.cc12703.app.docsetreader

import android.app.Application
import com.liulishuo.okdownload.core.Util
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    init {
        Util.enableConsoleLog()
    }
}
