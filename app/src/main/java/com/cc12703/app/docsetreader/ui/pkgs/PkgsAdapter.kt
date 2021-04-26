package com.cc12703.app.docsetreader.ui.pkgs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cc12703.app.docsetreader.databinding.PkgsListItemBinding
import com.cc12703.app.docsetreader.info.PkgInfo

class PkgsAdapter(private val handler: PkgHandler) : ListAdapter<PkgInfo, RecyclerView.ViewHolder>(PkgDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PkgViewHolder(
                PkgsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                handler
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pkgInfo = getItem(position)
        (holder as PkgViewHolder).bind(pkgInfo)
    }



    class PkgViewHolder(
            private val binding: PkgsListItemBinding,
            private val handler: PkgHandler
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                binding.pkgInfo?.let { pkgInfo ->
                    handler.onOpenPkg(pkgInfo)
                }
            }
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
        return oldItem == newItem
    }

}