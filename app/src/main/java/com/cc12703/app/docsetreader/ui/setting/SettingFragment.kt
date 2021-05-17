package com.cc12703.app.docsetreader.ui.setting

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.cc12703.app.docsetreader.R
import com.cc12703.app.docsetreader.databinding.SettingFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment: DialogFragment() {


    private val viewModel: SettingViewModel by viewModels()

    private lateinit var binding: SettingFragmentBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?
                             ): View? {

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        binding = SettingFragmentBinding.inflate(requireActivity().layoutInflater)
        binding.viewModel = viewModel
        builder.setTitle(resources.getString(R.string.setting_title))
            .setView(binding.root)
            .setPositiveButton(resources.getString(R.string.setting_btn_save))
                                { _, _ -> viewModel.save() }
            .setNegativeButton(resources.getString(R.string.setting_btn_cancel), null)

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }




}