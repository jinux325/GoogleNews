package com.u.googlenews.main

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.u.googlenews.LoadingDialog
import com.u.googlenews.NewsVO
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    private var list :ArrayList<NewsVO> = arrayListOf()
    private var liveDataMainList = MutableLiveData<ArrayList<NewsVO>>()
    private lateinit var dialog: Dialog
    private val requestUrl = "https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko"

    fun liveDataMainList(context: Context):MutableLiveData<ArrayList<NewsVO>>{
        dialog = LoadingDialog(context)
        dialog.show()
        CoroutineScope(Dispatchers.IO).launch{
           getRss()
        }
        return liveDataMainList
    }

    // RSS( https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko ) 주소의 title, link data를 파싱한다
    fun getRss() : ArrayList<NewsVO> {
        list.clear()
        try {
            var title = false
            var link = false
            var titleVal = ""
            var linkVal = ""
            val url = URL(requestUrl)
            val input = url.openStream()
            val factory =
                XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(InputStreamReader(input, "UTF-8"))

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.END_TAG -> if (parser.name == "item") {   // 태그 끝나면
                        getUrl(linkVal , titleVal)  // 제목, url 로 이미지, 내용 구하기
                        titleVal=""
                        linkVal=""
                    }
                    XmlPullParser.START_TAG -> {    // title, link 태그가 있으면 true
                        if (parser.name == "title") title = true
                        if (parser.name == "link") link = true
                    }
                    XmlPullParser.TEXT -> {     // true(태그가 있음)면 값 넣기
                        when {
                            title -> {
                                titleVal = parser.text
                                title = false
                            }
                            link -> {
                                linkVal = parser.text
                                link = false
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        dialog.dismiss()
        return list
    }

    /*
        url : 뉴스 url 주소
        title : 뉴스 제목
        라이브러리 : jsoup
        뉴스 주소를 받아서 image, description 태그의 data를 크롤링해 list에 넣고 liveData에도 넣어준다
    */
    private fun getUrl(url:String, title:String){
        try{
            val con: Connection = Jsoup.connect(url)
            val doc: Document = con.get()
            val ogImageContent = doc.select("meta[property=og:image]").attr("content")
            val ogTxtContent = doc.select("meta[property=og:description]").attr("content")

            val newsVO = NewsVO(
                url,
                title,
                ogTxtContent,
                ogImageContent,
                sortList(ogTxtContent)
            )
            list.add(newsVO)
            liveDataMainList.postValue(list)
        }catch (e: IOException){

        }

    }

    /*
        txt : 작업할 문장
        return : phrase return 값(중복된 구분 Map)을 정렬하여 반환
        phrase return값을 정렬하고 2글자 이상만 List에 넣어 return 해준다
    */
    fun sortList(phrase:String):ArrayList<String>{
        val expectedWordCount = phrase(phrase)

        val keywordList = ArrayList<String>()
        val sortedByValue = expectedWordCount.toList().sortedWith(compareBy({ -it.second }, { it.first })
        ).toMap()

        sortedByValue.toList().forEach{
            if(it.first.length >= 2){
                keywordList.add(it.first)
            }
        }
        return keywordList
    }
    /*
        phrase : 작업할 문장
        return : toWord return 값(공백으로 구분)을 중복된 횟수로 구분된 MAP 반환
        toWords에서 반환된 list에서 중복된 문장을 Map으로 묶어서 반환
    */
    private fun phrase(phrase: String): Map<String, Int> {
        return toWords(phrase).
            groupBy { it }.
            mapValues { it.value.size }
    }

    /*
        phrase : 작업할 문장
        return : 공백으로 잘라서 구분된 List 반환
        작업할 문장의 특수문자를 바꾸고 공백으로 잘라서 반환
    */
    private fun toWords(phrase: String): List<String> {
        return phrase.toLowerCase(Locale.getDefault()).replace(Regex("[^\uAC00-\uD7A3xfe0-9a-zA-Z%\\s]"), " ").trim().
            split(Regex("\\s+"))
    }


}