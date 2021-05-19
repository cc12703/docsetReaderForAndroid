package com.cc12703.app.docsetreader.worker

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cc12703.app.docsetreader.data.repo.PkgsRepository
import com.cc12703.app.docsetreader.data.repo.SettingRepository
import com.cc12703.app.docsetreader.util.LOG_TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

@HiltWorker
class LoadSettingWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val settingRepo: SettingRepository,
    private val pkgsRepo: PkgsRepository,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CFG_KEY_GITHUB_NAME = "github.name"
        const val CFG_KEY_TAG_PREFIX_NAME = "tag.prefix.name"
    }

    override suspend fun doWork(): Result {
        val login = settingRepo.githubName.first()
        Log.i(LOG_TAG, "setting githubname ${login}")

        if(login.isNotEmpty())
            return Result.success()

        val cfg = File(Environment.getExternalStorageDirectory(), "docsetreader.json")
        if(!cfg.exists())
            return Result.success()

        val jsonObj = JSONObject(cfg.readText())

        val githubName = jsonObj.getString(CFG_KEY_GITHUB_NAME)
        val tagPrefixName = jsonObj.getString(CFG_KEY_TAG_PREFIX_NAME)

        Log.i(LOG_TAG, "read ${githubName} - ${tagPrefixName}")

        settingRepo.save(githubName, tagPrefixName)
        GlobalScope.launch(Dispatchers.Main) {
            pkgsRepo.refreshPkgs()
        }

        return Result.success()
    }

}