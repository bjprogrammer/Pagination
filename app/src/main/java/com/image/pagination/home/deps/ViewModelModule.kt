package com.image.pagination.home.deps

import androidx.paging.*
import com.google.gson.Gson
import com.image.pagination.home.model.Image
import com.image.pagination.home.repository.ImageListDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ViewModelModule {
    @Provides
    fun getPager(dataSource: ImageListDataSource, config: PagingConfig): Pager<Int,Image.Page.Items.Content> {
        return Pager(config) {
            dataSource
        }
    }

    @Provides
    fun getGson(): Gson {
        return Gson()
    }

    @Provides
    fun getConfig(): PagingConfig{
        return PagingConfig(20,7, false)
    }
}

