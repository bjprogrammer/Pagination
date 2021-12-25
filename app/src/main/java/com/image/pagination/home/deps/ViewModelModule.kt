package com.image.pagination.home.deps

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.gson.Gson
import com.image.pagination.home.model.Image
import com.image.pagination.home.repository.ImageListDSFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ViewModelModule {
    @Provides
    fun getPagedList(dataSourceFactory: ImageListDSFactory, config: PagedList.Config): LiveData<PagedList<Image.Page.Items.Content>> {
        return LivePagedListBuilder(dataSourceFactory, config).build()
    }

    @Provides
    fun getGson(): Gson {
        return Gson()
    }

    @Provides
    fun getConfig(): PagedList.Config{
        return PagedList.Config.Builder()
                .setPrefetchDistance(7)
                .setEnablePlaceholders(false)
                .build()
    }
}

