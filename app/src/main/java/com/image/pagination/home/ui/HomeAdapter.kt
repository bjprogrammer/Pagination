package com.image.pagination.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.image.pagination.BR
import com.image.pagination.R
import com.image.pagination.home.viewmodel.HomeViewModel
import com.image.pagination.home.model.Image
import javax.inject.Inject

class HomeAdapter @Inject constructor() : PagingDataAdapter<Image.Page.Items.Content, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {
    lateinit var lifecycleOwner: LifecycleOwner
    var homeViewModel : HomeViewModel?=null
    fun setOwner(lifecycleOwner: LifecycleOwner){
        this.lifecycleOwner = lifecycleOwner
    }

    fun setViewModel(viewModel: HomeViewModel?) {
        homeViewModel = viewModel
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Image.Page.Items.Content> = object : DiffUtil.ItemCallback<Image.Page.Items.Content>() {
            override fun areItemsTheSame(oldList: Image.Page.Items.Content, newList: Image.Page.Items.Content): Boolean {
                return oldList.getName() == newList.getName() && oldList.getDrawable() == newList.getDrawable()
            }

            override fun areContentsTheSame(oldList: Image.Page.Items.Content, newList: Image.Page.Items.Content): Boolean {
                return areItemsTheSame(oldList, newList)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return ViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_feed, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.bind(getItem(i))
    }

    inner class ViewHolder internal constructor(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Any?) {
            binding.setVariable(BR.image, obj)
            binding.setVariable(BR.viewModel, homeViewModel)
            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }
    }
}