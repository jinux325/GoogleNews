package com.u.googlenews.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.u.googlenews.*
import com.u.googlenews.NewsVO
import com.u.googlenews.adapter.MainRvAdpater
import com.u.googlenews.databinding.ActivityMainBinding
import com.u.googlenews.detailPage.DetailPageActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel=ViewModelProvider.AndroidViewModelFactory(application).create(MainViewModel::class.java)

        // liveDataMainList observe
        viewModel.liveDataMainList(this@MainActivity).observe(this@MainActivity, Observer { list ->
            val adapter = MainRvAdpater(list)
            binding.recyclerView.adapter = adapter
            adapter.itemClick = object: NewsListeners {
                override fun onClick(view: View, position: Int) {
                    detailPageIntent(list, position)
                }
            }

        })

        // 당겨서 새로고침
        swipe.setOnRefreshListener{
            val dialog = LoadingDialog(this)
            dialog.show()
            GlobalScope.launch {
                CoroutineScope(Dispatchers.Unconfined).launch {
                    viewModel.getRss()  // Rss 데이터 다시 livedata에 넣기
                }
                runOnUiThread {
                    swipe.isRefreshing= false
                    dialog.dismiss()
                }
            }
        }
    }

    // 상세페이지로 이동
    private fun detailPageIntent(arr:ArrayList<NewsVO>, position:Int){
        Log.e("detailPageIntent ", " ${arr[position].title}   ${arr[position].txt}")
        val intent = Intent(this, DetailPageActivity::class.java)
        intent.putExtra("url", arr[position].url)
        intent.putExtra("title", arr[position].title)
        intent.putExtra("keyword", arr[position].keyword)
        startActivity(intent)
    }

}

