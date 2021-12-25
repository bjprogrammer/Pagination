package com.image.pagination.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.image.pagination.home.repository.ImageListDSFactory
import com.image.pagination.home.model.Image

class HomeViewModel @ViewModelInject constructor( var dataSourceFactory: ImageListDSFactory,var config: PagedList.Config, var images: LiveData<PagedList<Image.Page.Items.Content>> ) : ViewModel() {
    private val _search = MutableLiveData<String>()
    var search: LiveData<String> = _search

    fun setSearchText(searchText: String) {
        _search.value = searchText
        dataSourceFactory.setSearchQuery(searchText)
        images = LivePagedListBuilder(dataSourceFactory, config).build()
    }

    val errorLiveData: LiveData<String>
        get() = dataSourceFactory.errorLiveData

    val emptyListLiveData: LiveData<Image>
        get() = dataSourceFactory.emptyLiveData

    val titleLiveData: LiveData<String>
        get() = dataSourceFactory.titleLiveData

    val imageList: LiveData<PagedList<Image.Page.Items.Content>>
        get() = images
}