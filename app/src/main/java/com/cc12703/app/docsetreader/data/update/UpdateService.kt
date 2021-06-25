package com.cc12703.app.docsetreader.data.update

import android.util.Log
import com.cc12703.app.docsetreader.data.repo.PkgsRepository
import com.cc12703.app.docsetreader.data.repo.SettingRepository
import com.cc12703.app.docsetreader.info.PkgRemoteInfo
import com.cc12703.app.docsetreader.util.LOG_TAG
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateService @Inject constructor(
    private val settingRepo: SettingRepository,
    private val pkgRepo: PkgsRepository
) {


    private var sio: Socket? = null
    private var curAddr: String = ""


    private fun openSocketIO(addr: String) {
        Log.i(LOG_TAG, "openSocketIo ${addr}")

        val options = IO.Options.builder()
            .setTransports(arrayOf(WebSocket.NAME))
            .build()
        sio = IO.socket(addr, options)
        sio?.on("updateInfos") { args ->
            Log.i(LOG_TAG, "sio recv updateInfos")

            val infos = buildInfo(args[0] as JSONArray)
            if(infos.isNotEmpty()) {
                updatePkgs(infos)
            }
        }
        sio?.on(Socket.EVENT_CONNECT) { _ -> Log.i(LOG_TAG, "sio connect succ") }
        sio?.on(Socket.EVENT_CONNECT_ERROR) { _ -> Log.i(LOG_TAG, "sio connect fail") }
        sio?.connect()
    }

    private fun closeSocketIO() {
        sio?.disconnect()
        sio?.off()
    }


    private fun updatePkgs(pkgs: List<PkgRemoteInfo>) {
        Log.i(LOG_TAG, "updatePkgs num ${pkgs.size}")
        pkgRepo.updatePkgs(pkgs)
    }

    private fun buildInfo(datas: JSONArray): List<PkgRemoteInfo> {
        if(datas.length() == 0)
            return emptyList()

        val result = mutableListOf<PkgRemoteInfo>()
        for (index in 0 until datas.length()) {
            val item = datas.getJSONObject(index)
            val name = item.getString("repoName")
            val ver = item.getString("lastVer")
            val url = item.getString("lastUrl")
            result.add(PkgRemoteInfo(name, ver, url))
        }

        return result
    }


    private fun isCreateSocketIO(addr: String): Boolean {
        if(addr.isEmpty())
            return false

        if(curAddr.isEmpty())
            return true

        return curAddr != addr
    }

    suspend fun start() {
        settingRepo.docsetUpdateAddr.collect { value ->
            if (isCreateSocketIO(value)) {
                curAddr = value
                GlobalScope.launch(Dispatchers.IO) {
                    closeSocketIO()
                    openSocketIO(value)
                }
            }
        }
    }


    fun stop() {
        closeSocketIO()
    }


}