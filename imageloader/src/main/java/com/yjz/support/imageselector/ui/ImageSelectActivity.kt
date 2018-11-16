package com.yjz.support.imageselector.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.yjz.support.R
import com.yjz.support.imageselector.ImageSelector
import kotlinx.android.synthetic.main.activity_image_select_layout.*


class ImageSelectActivity: AppCompatActivity() {
    private val mSelector: ImageSelector by lazy {
        ImageSelector(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select_layout)

        aty_image_select_show_rv.layoutManager = GridLayoutManager(this, 3)
    }
}