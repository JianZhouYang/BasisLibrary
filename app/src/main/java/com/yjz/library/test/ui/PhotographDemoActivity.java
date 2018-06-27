package com.yjz.library.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.yjz.kotlin.util.CameraUtils;
import com.yjz.library.test.R;

import java.io.File;

public class PhotographDemoActivity extends AppCompatActivity {
    private ImageView mShowPhotoIv;
    private String savePath;
    private String cutSavePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph_demo);

        savePath = getExternalFilesDir(null) + "/111.jpg";
        cutSavePath = getExternalFilesDir(null) + "/222.jpg";

        mShowPhotoIv = findViewById(R.id.iv_show_photo);

        findViewById(R.id.btn_take_a_photo).setOnClickListener(v -> {
            CameraUtils.INSTANCE.photograph(PhotographDemoActivity.this, savePath, CameraUtils.PHOTO_GRAPH_REQUEST);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtils.PHOTO_GRAPH_REQUEST && resultCode == Activity.RESULT_OK) {
            CameraUtils.INSTANCE.cutting(this, new File(savePath), cutSavePath, 100, 100, CameraUtils.PHOTO_CUTTING_REQUEST);
        } else if (requestCode == CameraUtils.PHOTO_CUTTING_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(cutSavePath);
            if (null != bitmap) {
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                mShowPhotoIv.setImageDrawable(drawable);
            }
        }
    }
}
