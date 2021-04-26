

package com.cc12703.app.docsetreader

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.cc12703.app.docsetreader.info.PkgInfo
import com.cc12703.app.docsetreader.ui.pkgs.PkgHandler
import com.cc12703.app.docsetreader.ui.pkgs.PkgsFragment
import com.cc12703.app.docsetreader.ui.read.ReadFragment
import com.cc12703.app.docsetreader.util.LOG_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = if(resources.getBoolean(R.bool.has_two_panes)) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        setContentView(R.layout.main_activity)

        val pkgFrag = supportFragmentManager.findFragmentById(R.id.pkg_frag) as PkgsFragment
        pkgFrag.setPkgHander(object : PkgHandler {
            override fun onOpenPkg(pkg: PkgInfo) {
                Log.i(LOG_TAG, "openPkg ${pkg.path}")
                if(resources.getBoolean(R.bool.has_two_panes)) {
                    openPkgForTwoPanes(pkg)
                }
                else {
                    openPkgForOnePane(pkg)
                }
            }
        })

    }

    private fun openPkgForOnePane(pkg: PkgInfo) {
        supportFragmentManager.commit {
            replace(R.id.pkg_frag, ReadFragment.newInstance(pkg.path.absolutePath))
            addToBackStack(null)
        }
    }

    private fun openPkgForTwoPanes(pkg: PkgInfo) {
        val slidingPane = findViewById<SlidingPaneLayout>(R.id.sliding_pane)
        supportFragmentManager.commit {
            replace(R.id.read_frag, ReadFragment.newInstance(pkg.path.absolutePath))
            if(slidingPane.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        slidingPane.open()
    }


}
