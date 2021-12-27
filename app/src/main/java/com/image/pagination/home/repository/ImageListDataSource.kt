package com.image.pagination.home.repository

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.image.pagination.home.model.Image
import com.image.pagination.utils.HelperFunctions.fetchData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class ImageListDataSource @Inject constructor(@ApplicationContext val context: Context): PagingSource<Int, Image.Page.Items.Content>() {
    private var searchQuery = ""

    @Inject
    lateinit var gson: Gson

    fun setSearchQuery(query: String) {
        searchQuery = query
        invalidate()   //refresh pagination list, calls load method if there is any difference from previous list
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image.Page.Items.Content> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 1
            val jsonData = fetchData(context, "CONTENTLISTINGPAGE-PAGE$nextPage.json")
            var imageList = mutableListOf<Image.Page.Items.Content>()

            if (jsonData != null) {
                val image = gson.fromJson(jsonData, Image::class.java)
                imageList = image.getPage().getItems().getContent()
            }

            var nextKey :Int?= nextPage+1
            if(imageList.isEmpty()){
                nextKey = null  //To mark last position and stop further loading
            }

            return LoadResult.Page(
                data = imageList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Image.Page.Items.Content>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}