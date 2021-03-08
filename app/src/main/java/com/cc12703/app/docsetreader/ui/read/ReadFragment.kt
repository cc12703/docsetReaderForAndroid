package com.cc12703.app.docsetreader.ui.read

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cc12703.app.docsetreader.data.PkgContentRepository
import com.cc12703.app.docsetreader.databinding.ReadFragmentBinding
import com.cc12703.app.docsetreader.util.BackPressHandler
import com.cc12703.app.docsetreader.util.LOG_TAG
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ReadFragment : Fragment(), BackPressHandler {

    private val args: ReadFragmentArgs by navArgs()

    private val repo: PkgContentRepository by lazy {
        PkgContentRepository.get(File(args.docsetPath))
    }

    private lateinit var webView: ReadWebView


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = ReadFragmentBinding.inflate(inflater, container, false)

        this.webView = addWebview(binding)
        val startUrl = repo.getIndexPageUrl()
        this.webView.loadRelaUrl(startUrl)

        return binding.root
    }


    private fun addWebview(binding: ReadFragmentBinding): ReadWebView {
        val webView = ReadWebView(this.requireContext(), repo)
        val layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        binding.rootView.addView(webView, layoutParams)
        return webView
    }

    override fun onBackPress(): Boolean {
        if(this.webView.canGoBack()) {
            this.webView.goBack()
            return true
        }

        return false
    }

}