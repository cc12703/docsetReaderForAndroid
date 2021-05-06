package com.cc12703.app.docsetreader.ui.pkgs

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cc12703.app.docsetreader.databinding.PkgsListItemBinding
import com.cc12703.app.docsetreader.info.PkgInfo
import com.cc12703.app.docsetreader.util.LOG_TAG

class PkgsAdapter(private val viewModel: PkgsViewModel,
                 private val lifecycleOwner: LifecycleOwner) : ListAdapter<PkgInfo, RecyclerView.ViewHolder>(PkgDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PkgsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = lifecycleOwner
        return PkgViewHolder( binding, viewModel )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pkgInfo = getItem(position)
        (holder as PkgViewHolder).bind(pkgInfo)
    }



    class PkgViewHolder(
            private val binding: PkgsListItemBinding,
            private val viewModel: PkgsViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = viewModel
        }


        fun bind(item: PkgInfo) {
            binding.apply {
                pkgInfo = item
                executePendingBindings()
            }
        }
    }

}



private class PkgDiffCallback: DiffUtil.ItemCallback<PkgInfo>() {
    override fun areItemsTheSame(oldItem: PkgInfo, newItem: PkgInfo): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: PkgInfo, newItem: PkgInfo): Boolean {
        return oldItem.name == newItem.name
                && oldItem.dlVer == newItem.dlVer
                && oldItem.dlPercent == newItem.dlPercent
                && oldItem.lastVer == newItem.lastVer
    }

}