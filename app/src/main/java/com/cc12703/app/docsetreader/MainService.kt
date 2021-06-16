package com.cc12703.app.docsetreader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.cc12703.app.docsetreader.data.repo.SettingRepository
import com.cc12703.app.docsetreader.data.update.UpdateService
import com.cc12703.app.docsetreader.util.CHANNEL_ID_MAIN
import com.cc12703.app.docsetreader.util.LOG_TAG
import com.cc12703.app.docsetreader.util.SERVICE_ID_MAIN
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.SocketIOException
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainService : Service() {

    @Inject
    lateinit var updater: UpdateService


    override fun onCreate() {
        super.onCreate()

        setForeground()

        GlobalScope.launch(Dispatchers.Main) {
           updater.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        updater.stop()
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun setForeground() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID_MAIN, CHANNEL_ID_MAIN,
                                            NotificationManager.IMPORTANCE_DEFAULT)
            val mgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mgr.createNotificationChannel(channel)
        }

        val notify = NotificationCompat.Builder(this, CHANNEL_ID_MAIN)
            .setTicker("Notify")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Main Service")
            .setContentText("Main Service")
            .build()
        startForeground(SERVICE_ID_MAIN, notify)
    }
}