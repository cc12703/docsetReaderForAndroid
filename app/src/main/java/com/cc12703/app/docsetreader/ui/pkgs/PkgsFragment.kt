package com.cc12703.app.docsetreader.ui.pkgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cc12703.app.docsetreader.databinding.PkgsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PkgsFragment : Fragment() {

    private  val viewModel: PkgsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = PkgsFragmentBinding.inflate(inflater, container, false)

        val adapter = PkgsAdapter()
        binding.pkgList.adapter = adapter

        viewModel.pkgs.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}