package com.cc12703.app.docsetreader.ui.pkgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.cc12703.app.docsetreader.databinding.PkgsFragmentBinding
import com.cc12703.app.docsetreader.info.PkgInfo
import com.cc12703.app.docsetreader.ui.common.RetryCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PkgsFragment : Fragment() {

    private val viewModel: PkgsViewModel by viewModels()

    private lateinit var pkgHandler: PkgHandler

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = PkgsFragmentBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = PkgsAdapter(viewModel, this)
        binding.pkgList.adapter = adapter
        binding.pkgList.addItemDecoration(
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL )
        )

        binding.retryCallback = object: RetryCallback {
            override fun retry() {
                viewModel.refreshPkgs()
            }
        }

        viewModel.setPkgHander(object : PkgHandler {
            override fun onOpenPkg(pkg: PkgInfo) {
                pkgHandler.onOpenPkg(pkg)
            }
        })

        viewModel.getPkg().observe(viewLifecycleOwner) {
            adapter.submitList(it.data)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.requestPkgs()
    }


    fun setPkgHander(handler: PkgHandler) {
        pkgHandler = handler

    }
}