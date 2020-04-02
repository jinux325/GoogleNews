package com.u.googlenews.detailPage

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.u.googlenews.BR
import com.u.googlenews.R
import com.u.googlenews.NewsVO
import com.u.googlenews.databinding.ActivityDetailPageBinding
import kotlinx.android.synthetic.main.activity_detail_page.*


class DetailPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail_page)

        // intent data 가져오기
        val intentKetwordList = intent.getStringArrayListExtra("keyword")
        val intentTitle = intent.getStringExtra("title")
        val intentUrl = intent.getStringExtra("url")

        val newsVO = NewsVO(
            intentUrl,
            intentTitle,
            "",
            "",
            intentKetwordList
        )
        binding.setVariable(BR.item, newsVO)

        // 웹뷰 설정
        detail_webview.webViewClient = WebViewClient()
        val settings = detail_webview.settings
        settings.setSupportMultipleWindows(true)
        settings.setSupportZoom(true)
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true


    }


}
