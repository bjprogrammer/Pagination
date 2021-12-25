package com.image.pagination.home.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.image.pagination.home.model.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageListDSFactory @Inject internal constructor(@ApplicationContext context: Context) : DataSource.Factory<Int, Image.Page.Items.Content>() {
    @Inject lateinit var imageListDataSource: ImageListDataSource

    override fun create(): DataSource<Int, Image.Page.Items.Content> {
        return imageListDataSource
    }

    val errorLiveData: MutableLiveData<String>
        get() = imageListDataSource.errorLiveData

    val emptyLiveData: MutableLiveData<Image>
        get() = imageListDataSource.emptyListLiveData

    val titleLiveData: MutableLiveData<String>
        get() = imageListDataSource.titleLiveData

    fun setSearchQuery(query: String) {
        imageListDataSource.setSearchQuery(query)
    }
}