package com.u.googlenews

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsVO (
    val url:String,                 // 게시글 link url 주소
    val title:String,               // 게시글 제목
    val txt:String,                 // 게시글 내용
    val thumbnail:String,           // 게시글 썸네일
    val keyword:ArrayList<String>   // 키워드 list ( 상위 3개 사용 )
) : Parcelable