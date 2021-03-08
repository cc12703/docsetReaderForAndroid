

package com.cc12703.app.docsetreader

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.cc12703.app.docsetreader.databinding.MainActivityBinding
import com.cc12703.app.docsetreader.util.BackPressHandler
import com.cc12703.app.docsetreader.util.LOG_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<MainActivityBinding>(this, R.layout.main_activity)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val navFrag = supportFragmentManager.findFragmentById(R.id.nav_host)
        val curFrag = navFrag?.childFragmentManager?.primaryNavigationFragment
        if(curFrag is BackPressHandler) {
            if(curFrag.onBackPress())
                return true
        }

        return super.onKeyDown(keyCode, event)
    }

}
