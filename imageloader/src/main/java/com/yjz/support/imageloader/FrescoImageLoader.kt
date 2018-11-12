package com.yjz.support.imageloader

import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import java.io.File

class FrescoImageLoader(private val mConfig: ImageLoaderConfig) : ILoader<SimpleDraweeView>{
    private val mFrescoConfig: FrescoConfig = FrescoConfig();

    init {
        Fresco.initialize(mConfig.getContext(), mFrescoConfig.getImagePipelineConfig(mConfig.getContext(), mConfig))
    }

    override fun display(view: SimpleDraweeView, url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun display(view: SimpleDraweeView, url: String, width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUrl(url: Any, type: UrlType): String = when (type){
        UrlType.IMG_TYPE_NETWORK -> {
            url as String
        }
        UrlType.IMG_TYPE_LOCAL -> {
            "file://$url";
        }
        UrlType.IMG_TYPE_RES -> {
            val resId = url as Int
            "res://" + mConfig.getContext().packageName + "/" + resId
        }
        UrlType.IMG_TYPE_ASE -> {
            "asset://$url"
        }
    }

    override fun clearCache(url: String) {
        val uri: Uri = Uri.parse(url);
        Fresco.getImagePipeline().evictFromMemoryCache(uri);
        Fresco.getImagePipeline().evictFromDiskCache(uri);
    }

    override fun clearAllCache() {
        Fresco.getImagePipeline().clearCaches();
    }

    override fun getImageFileFromCache(url: String): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}