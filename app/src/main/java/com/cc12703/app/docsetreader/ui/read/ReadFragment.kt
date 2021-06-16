package com.cc12703.app.docsetreader.ui.read

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.cc12703.app.docsetreader.data.repo.PkgContentRepository
import com.cc12703.app.docsetreader.databinding.ReadFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ReadFragment : Fragment() {


    companion object {
        fun newInstance(docsetPath: String): ReadFragment {
            val frag = ReadFragment()
            frag.arguments = Bundle().apply {
                putString("docsetPath", docsetPath)
            }
            return frag
        }
    }

    private val repo: PkgContentRepository by lazy {
        PkgContentRepository.get(File(requireArguments().getString("docsetPath")))
    }

    private lateinit var webView: ReadWebView

    private val backPressCB: OnBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            doBackPressed()
        }
    }


    private fun doBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            backPressCB.isEnabled = false
            requireActivity().onBackPressed()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCB)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = ReadFragmentBinding.inflate(inflater, container, false)
        if(arguments == null)
            return binding.root

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

}