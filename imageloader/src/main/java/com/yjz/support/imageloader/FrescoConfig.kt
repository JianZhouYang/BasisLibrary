package com.yjz.support.imageloader

import android.content.Context
import android.os.Build
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.internal.Supplier
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import java.io.File

class FrescoConfig {

    private companion object {
        const val MAX_SMALL_DISK_CACHE_SIZE: Int = 20 * ByteConstants.MB;//小图磁盘缓存的最大值
        const val MAX_SMALL_DISK_LOW_CACHE_SIZE: Int = 10 * ByteConstants.MB;//小图低磁盘空间缓存的最大值
        const val MAX_SMALL_DISK_VERYLOW_CACHE_SIZE: Int = 5 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值
        const val IMAGE_PIPELINE_SMALL_CACHE_DIR: String = "small_cache";//小图所放路径的文件夹名

        const val MAX_DISK_CACHE_VERYLOW_SIZE = 10 * ByteConstants.MB//默认图极低磁盘空间缓存的最大值
        const val MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB//默认图低磁盘空间缓存的最大值
        const val MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB//默认图磁盘缓存的最大值
        const val IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache"//默认图所放路径的文件夹名
    }



    fun getImagePipelineConfig(context: Context, imageLoaderConfig: ImageLoaderConfig): ImagePipelineConfig?{

        val supplierMemoryCacheParams: Supplier<MemoryCacheParams> = Supplier<MemoryCacheParams> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MemoryCacheParams(getMaxCacheSize(), 56, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            } else {
                MemoryCacheParams(getMaxCacheSize(), 256, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        }


        //小图片的磁盘配置
        val diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.applicationContext.cacheDir)//缓存图片基路径  new File(Constants.ROOT_PATH)
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
                //.setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
                //.setCacheEventListener(cacheEventListener)//缓存事件侦听器。
                //.setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(MAX_SMALL_DISK_CACHE_SIZE.toLong())//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE.toLong())//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE.toLong())//缓存的最大大小,当设备极低磁盘空间
                //.setVersion(version)
                .build()

        //默认图片的磁盘配置
        val diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(File(imageLoaderConfig.getBaseDirectoryPath()))//缓存图片基路径
                .setBaseDirectoryName(imageLoaderConfig.getBaseDirectoryName())//文件夹名
                //.setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
                //.setCacheEventListener(cacheEventListener)//缓存事件侦听器。
                //.setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE.toLong())//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE.toLong())//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE.toLong())//缓存的最大大小,当设备极低磁盘空间
                //.setVersion(version)
                .build()


        return if (null != imageLoaderConfig.getOkHttpClient()) {
            OkHttpImagePipelineConfigFactory
                    .newBuilder(context, imageLoaderConfig.getOkHttpClient())
                    .setDownsampleEnabled(true)
                    .setBitmapMemoryCacheParamsSupplier(supplierMemoryCacheParams)//内存缓存配置（一级缓存，已解码的图片）
                    .setMainDiskCacheConfig(diskCacheConfig)//磁盘缓存配置（总，三级缓存）
                    .setSmallImageDiskCacheConfig(diskSmallCacheConfig)//磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
                    .build();
        } else {
            ImagePipelineConfig.newBuilder(context)
                    .setDownsampleEnabled(true)
                    //.setAnimatedImageFactory(AnimatedImageFactory animatedImageFactory)//图片加载动画
                    .setBitmapMemoryCacheParamsSupplier(supplierMemoryCacheParams)//内存缓存配置（一级缓存，已解码的图片）
                    //.setCacheKeyFactory(cacheKeyFactory)//缓存Key工厂
                    //.setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)//内存缓存和未解码的内存缓存的配置（二级缓存）
                    //.setExecutorSupplier(executorSupplier)//线程池配置
                    //.setImageCacheStatsTracker(imageCacheStatsTracker)//统计缓存的命中率
                    //.setImageDecoder(ImageDecoder imageDecoder) //图片解码器配置
                    //.setIsPrefetchEnabledSupplier(Supplier<Boolean> isPrefetchEnabledSupplier)//图片预览（缩略图，预加载图等）预加载到文件缓存
                    .setMainDiskCacheConfig(diskCacheConfig)//磁盘缓存配置（总，三级缓存）
                    //.setMemoryTrimmableRegistry(memoryTrimmableRegistry) //内存用量的缩减,有时我们可能会想缩小内存用量。比如应用中有其他数据需要占用内存，不得不把图片缓存清除或者减小 或者我们想检查看看手机是否已经内存不够了。
                    //.setNetworkFetchProducer(networkFetchProducer)//自定的网络层配置：如OkHttp，Volley
                    //.setPoolFactory(poolFactory)//线程池工厂配置
                    //.setProgressiveJpegConfig(progressiveJpegConfig)//渐进式JPEG图
                    //.setRequestListeners(requestListeners)//图片请求监听
                    //.setResizeAndRotateEnabledForNetwork(boolean resizeAndRotateEnabledForNetwork)//调整和旋转是否支持网络图片
                    .setSmallImageDiskCacheConfig(diskSmallCacheConfig)//磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
                    .build();
        }

    }


    private fun getMaxCacheSize(): Int{
        return 0;
    }


}