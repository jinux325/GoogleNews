package com.u.googlenews.dataBinding

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.u.googlenews.R

// 썸네일 이미지 있으면 보여주기
@BindingAdapter("thumbnail")
fun bindImage(view: ImageView, image:String?) {
    Log.e("bindImage "," $image")
    if(image == ""|| image==null){
        Glide.with(view.context).load(R.drawable.img).into(view)
    }else{
        Glide.with(view.context).load(image).into(view)
    }

}

