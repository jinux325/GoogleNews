package com.u.googlenews

import android.view.View

interface NewsListeners {
    fun onClick(view: View, position: Int)
}