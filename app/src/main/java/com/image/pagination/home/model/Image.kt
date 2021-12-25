package com.image.pagination.home.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.image.pagination.BR
import java.util.*

class Image : BaseObservable() {
    private lateinit var page: Page

    @Bindable
    fun getPage(): Page {
        return page
    }

    fun setPage(page: Page) {
        this.page = page
        notifyPropertyChanged(BR.page)
    }

    inner class Page : BaseObservable() {
        private lateinit var title: String

        @SerializedName("page-size")
        private lateinit var pageSize: String

        @SerializedName("total-content-items")
        private lateinit var totalSize: String

        @SerializedName("page-num")
        private lateinit var pageNo: String

        @SerializedName("content-items")
        private lateinit var items: Items

        @Bindable
        fun getTitle(): String {
            return title
        }

        fun setTitle(title: String) {
            this.title = title
            notifyPropertyChanged(BR.title)
        }

        @Bindable
        fun getPageSize(): String {
            return pageSize
        }

        fun setPageSize(pageSize: String) {
            this.pageSize = pageSize
            notifyPropertyChanged(BR.pageSize)
        }

        @Bindable
        fun getTotalSize(): String {
            return totalSize
        }

        fun setTotalSize(totalSize: String) {
            this.totalSize = totalSize
            notifyPropertyChanged(BR.totalSize)
        }

        @Bindable
        fun getPageNo(): String {
            return pageNo
        }

        fun setPageNo(pageNo: String) {
            this.pageNo = pageNo
            notifyPropertyChanged(BR.pageNo)
        }

        @Bindable
        fun getItems(): Items {
            return items
        }

        fun setItems(items: Items) {
            this.items = items
            notifyPropertyChanged(BR.items)
        }

        inner class Items : BaseObservable() {
            private lateinit var content: ArrayList<Content>

            @Bindable
            fun getContent(): ArrayList<Content> {
                return content
            }

            fun setContent(content: ArrayList<Content>) {
                this.content = content
                notifyPropertyChanged(BR.content)
            }

            inner class Content : BaseObservable() {
                private lateinit var name: String

                @SerializedName("poster-image")
                private lateinit var drawable: String

                @Bindable
                fun getName(): String {
                    return name
                }

                fun setName(name: String) {
                    this.name = name
                    notifyPropertyChanged(BR.name)
                }

                @Bindable
                fun getDrawable(): String {
                    return drawable
                }

                fun setDrawable(drawable: String) {
                    this.drawable = drawable
                    notifyPropertyChanged(BR.drawable)
                }
            }
        }
    }
}