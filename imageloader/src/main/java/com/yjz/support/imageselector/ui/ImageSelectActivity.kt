package com.yjz.support.imageselector.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import com.yjz.support.R
import com.yjz.support.imageselector.FileItem
import com.yjz.support.imageselector.ImageSelector
import com.yjz.support.imageselector.callback.ImageCallback
import com.yjz.support.imageselector.ui.adapter.ImageSelectAdapter
import kotlinx.android.synthetic.main.activity_image_select_layout.*


class ImageSelectActivity: AppCompatActivity() {
    private val mSelector: ImageSelector by lazy {
        ImageSelector(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select_layout)

        aty_image_select_show_rv.layoutManager = GridLayoutManager(this, 3)


        mSelector.getAllImages(object: ImageCallback {
            override fun onQueryStart() {
                Toast.makeText(this@ImageSelectActivity, "onQueryStart", Toast.LENGTH_SHORT).show()
            }

            override fun onQueryFinish(list: MutableList<FileItem>?) {
                list?.let {
                    this@ImageSelectActivity.runOnUiThread{
                        val adapter = ImageSelectAdapter(this@ImageSelectActivity, list)
                        aty_image_select_show_rv.adapter = adapter
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mSelector.destroy()
    }
}