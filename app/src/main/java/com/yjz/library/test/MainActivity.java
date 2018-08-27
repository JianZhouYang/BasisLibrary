package com.yjz.library.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yjz.http.HttpRequest;
import com.yjz.http.HttpUtils;
import com.yjz.http.callback.DownloadCallback;
import com.yjz.http.client.DefaultOkHttpClient;
import com.yjz.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_main_jump).setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, PhotographDemoActivity.class));
            startActivity(new Intent(MainActivity.this, UITestActivity.class));
        });

//        ImageLoaderConfig imageLoaderConfig = new ImageLoaderConfig.Builder(this)
//                .setBaseDirectoryPath(getExternalFilesDir(null).getAbsolutePath())
//                .setDownloader(path -> {
//                    Log.e("yjz", "path===>" + path);
//                    Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
//                })
//                .setLoader(new FrescoImageLoader())
//                .create();
//        String path = imageLoaderConfig.getBaseDirectoryPath();
//        Log.e("yjz", "path===>" + path);
//        imageLoaderConfig.getDownLoader().downLoad("my name is yjz");

        HttpUtils.INSTANCE.initialization(new DefaultOkHttpClient());


        File saveFile = new File(getExternalFilesDir(null).getAbsolutePath() + "/777.jpg");
        String url = "http://f.hiphotos.baidu.com/image/pic/item/d833c895d143ad4bb40a080e8e025aafa50f06d3.jpg";
        HttpRequest request = new HttpRequest.DownloadFileBuilder(saveFile).setUrl(url).create();
        HttpUtils.INSTANCE.download(request, new DownloadCallback() {
            @Override
            public void onProgress(long bytesRead, long contentLength, boolean isDone) {
                Log.e("yjz", "bytesRead--->" + bytesRead + "  contentLength--->" + contentLength + "  isDone--->" + isDone);
            }

            @Override
            public void onSuccess(int code, @org.jetbrains.annotations.Nullable String content, @org.jetbrains.annotations.Nullable byte[] byteArray) {
                Log.e("yjz", "code--->" + code + "  content===>" + content);
            }

            @Override
            public void onError(int code, @NotNull String message) {
                Log.e("yjz", "code--->" + code + "  message===>" + message);

            }
        });
    }
}
