package com.yjz.support.imageselector.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.yjz.support.imageloader.R
import com.yjz.support.imageselector.base.FileItem
import com.yjz.support.imageselector.ImageSelector
import com.yjz.support.imageselector.callback.ImageCallback
import com.yjz.support.imageselector.ui.adapter.ImageSelectAdapter
import kotlinx.android.synthetic.main.activity_image_select_layout.*


class ImageSelectActivity: AppCompatActivity() {
    private val data_key = "data.key"

    private val mSelector: ImageSelector by lazy {
        ImageSelector(this)
    }

    fun jump2Me(activity: Activity, enabledCamera: Boolean){
        val intent = Intent(activity, ImageSelectActivity::class.java)
        intent.putExtra(data_key, enabledCamera)
        activity.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select_layout)

        val enabledCamera = intent.getBooleanExtra(data_key, true)

        aty_image_select_show_rv.layoutManager = GridLayoutManager(this, 3)
        aty_image_select_show_rv.isEnabled

        mSelector.setImageCallback(object: ImageCallback {
            override fun onQueryStart() {
                Toast.makeText(this@ImageSelectActivity, "onQueryStart", Toast.LENGTH_SHORT).show()
            }

            override fun onQueryFinish(type: ImageCallback.Type, list: MutableList<FileItem>?) {
                list?.let {
                    this@ImageSelectActivity.runOnUiThread{
                        if (type == ImageCallback.Type.TYPE_ALL_IMAGES) {
                            if (enabledCamera) {
                                val item = FileItem("", "", "", "")
                                item.actionType = FileItem.ACTION_TYPE_CAMERA
                                list.add(0, item)
                            }
                            val adapter = ImageSelectAdapter(this@ImageSelectActivity, list)
                            aty_image_select_show_rv.adapter = adapter
                        }
                    }
                }
            }
        })

        mSelector.getAllImages()
    }

}