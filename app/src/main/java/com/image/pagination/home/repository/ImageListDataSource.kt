package com.image.pagination.home.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.image.pagination.R
import com.image.pagination.home.model.Image
import com.image.pagination.utils.HelperFunctions.fetchData
import com.image.pagination.utils.PageType
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


class ImageListDataSource @Inject internal constructor(@ApplicationContext private val context: Context) : PageKeyedDataSource<Int, Image.Page.Items.Content>() {
    private var ITEM_COUNT = 0
    private var searchQuery = ""
    @Inject lateinit var gson: Gson

    val errorLiveData = MutableLiveData<String>()
    val emptyListLiveData = MutableLiveData<Image>()
    val titleLiveData = MutableLiveData<String>()

    companion object {
        private const val INIT_PAGE = 1
    }

    fun setSearchQuery(query: String) {
        searchQuery = query
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Image.Page.Items.Content>) {
        addPage(INIT_PAGE, PageType.INITIAL, callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Image.Page.Items.Content>) {
        addPage(params.key, PageType.LAST, callback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Image.Page.Items.Content>) {
        addPage(params.key, PageType.NEXT, callback)
    }

    @Suppress("UNCHECKED_CAST")
    private fun addPage(pageNo: Int, pageType: PageType, callback: Any) {
        val jsonData = fetchData(context, "CONTENTLISTINGPAGE-PAGE$pageNo.json")

        if (jsonData != null) {
            val image = gson.fromJson(jsonData, Image::class.java)

            if (image.getPage().getItems().getContent().size == 0)
                emptyListLiveData.postValue(image)
            else {
                titleLiveData.postValue(image.getPage().getTitle())
                var imageList: List<Image.Page.Items.Content>? = image.getPage().getItems().getContent()

                if(searchQuery.isNotEmpty()) {
                    val filteredList: MutableList<Image.Page.Items.Content> = ArrayList()
                    for (item in imageList!!) {
                        if (item.getName().toLowerCase(Locale.getDefault()).contains(searchQuery.toLowerCase(Locale.getDefault())))
                            filteredList.add(item)
                    }

                    imageList = filteredList
                }

                when (pageType) {
                    PageType.LAST ->
                        if (callback is LoadCallback<*, *>) {
                            ITEM_COUNT -= image.getPage().getPageSize().toInt()

                            if (pageNo > 1)
                                (callback as LoadCallback<Int, Image.Page.Items.Content>).onResult(imageList!!, pageNo - 1)
                            else
                                (callback as LoadCallback<Int, Image.Page.Items.Content>).onResult(imageList!!, null)
                        }

                    PageType.INITIAL ->
                        if (callback is LoadInitialCallback<*, *>) {
                            ITEM_COUNT = image.getPage().getPageSize().toInt()

                            if (ITEM_COUNT < image.getPage().getTotalSize().toInt())
                                (callback as LoadInitialCallback<Int, Image.Page.Items.Content>).onResult(imageList!!, null, pageNo + 1)
                            else
                                (callback as LoadInitialCallback<Int, Image.Page.Items.Content>).onResult(imageList!!, null, null)
                        }

                    PageType.NEXT ->
                        if (callback is LoadCallback<*, *>) {
                            ITEM_COUNT += image.getPage().getPageSize().toInt()

                            if (ITEM_COUNT < image.getPage().getTotalSize().toInt())
                                (callback as LoadCallback<Int, Image.Page.Items.Content>).onResult(imageList!!, pageNo + 1)
                            else
                                (callback as LoadCallback<Int, Image.Page.Items.Content>).onResult(imageList!!, null)
                        }
                }
            }
        } else
            errorLiveData.postValue(context.getString(R.string.error_message))
    }

}