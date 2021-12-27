package com.image.pagination.home.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.image.pagination.home.model.Image
import com.image.pagination.home.repository.ImageListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val dataSource: ImageListDataSource, val pager:Pager<Int, Image.Page.Items.Content>) : ViewModel() {
    private val _search = MutableLiveData("")
    var search: LiveData<String> = _search

    fun setSearchText(searchText: String) {
        _search.value = searchText
//        dataSource.setSearchQuery(searchText)
        mediatorLiveData.updateSourceFiltering(searchText)
    }

    private var imageList = pager.liveData.cachedIn(viewModelScope)
    val mediatorLiveData =
        MediatorLiveData<PagingData<Image.Page.Items.Content>>().apply { updateSourceFiltering("") }

    private fun MediatorLiveData<PagingData<Image.Page.Items.Content>>.updateSourceFiltering(searchText: String) {
        removeSource(imageList)
        addSource(imageList) {
            this.value = it.filter {
                return@filter it.getName().lowercase().contains(searchText)
            }
        }
    }
}