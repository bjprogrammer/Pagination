package com.image.pagination.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.loadingview.LoadingView
import com.image.pagination.R
import com.image.pagination.databinding.ActivityMainBinding
import com.image.pagination.utils.Constants.LANDSCAPE_GRID_SIZE
import com.image.pagination.utils.Constants.PORTRAIT_GRID_SIZE
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import com.image.pagination.home.model.Image
import com.image.pagination.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var mAdapter: HomeAdapter
    val homeViewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var postList: RecyclerView
    private lateinit var progressBar: LoadingView
    private lateinit var emptyView: View
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        attachViews()
        configRecyclerView()
    }

    private fun attachViews() {
        postList = binding.imageList
        progressBar = binding.loadingView
        emptyView = binding.emptyView
        title = binding.toolbarTextView

        attachToolbar()
    }

    private fun attachToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    private fun configRecyclerView() {
        val gridLayoutManager: GridLayoutManager = if (resources.configuration.orientation == 1)
            GridLayoutManager(this, PORTRAIT_GRID_SIZE)
        else
            GridLayoutManager(this, LANDSCAPE_GRID_SIZE)

        mAdapter.setOwner(this@HomeActivity)

        postList.apply {
            layoutManager = gridLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }


        homeViewModel.mediatorLiveData.observe(this@HomeActivity){
            mAdapter.setViewModel(homeViewModel)
            lifecycleScope.launch {
                mAdapter.submitData(it)
            }
        }


        mAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading){
                progressBar.visibility = View.VISIBLE
                progressBar.start()
            }
            else{
                progressBar.visibility = View.GONE
                progressBar.stop()

                // getting the error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun fromView(searchView: SearchView, searchItem: MenuItem): Observable<String> {
        val subject = PublishSubject.create<String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!searchView.isIconified)
                    searchView.isIconified = true

                searchView.clearFocus()
                searchItem.collapseActionView()
                subject.onComplete()
                return false
            }

            override fun onQueryTextChange(search: String): Boolean {
                subject.onNext(search)
                return false
            }
        })
        return subject
    }

    @SuppressLint("CheckResult")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        fromView(searchView, searchItem)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { text: String ->
                    if (text.isEmpty())
                        return@map "EMPTY CASE"
                    else
                        return@map text.lowercase(Locale.ROOT).trim()
                }
                .filter { text: String -> text.length >= 3 }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : io.reactivex.Observer<String> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(search: String) {
                        if (search == "EMPTY CASE")
                            homeViewModel.setSearchText("")
                        else
                            homeViewModel.setSearchText(search)
                    }

                    override fun onError(e: Throwable) {}

                    override fun onComplete() {}
                })

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

