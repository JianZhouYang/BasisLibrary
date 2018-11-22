package com.yjz.support.imageselector.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.yjz.support.imageloader.R;
import com.yjz.support.imageselector.base.FileItem;
import com.yjz.support.imageselector.adapterbase.RecyclerDefaultAdapter;
import com.yjz.support.imageselector.base.MemoryCache;

import java.util.List;

public class ImageSelectAdapter extends RecyclerDefaultAdapter<FileItem> {
    private int mWidth;
    private MemoryCache mMemoryCache;

    public ImageSelectAdapter(Context context) {
        super(context);
        init();
    }

    public ImageSelectAdapter(Context context, List<FileItem> list) {
        super(context, list);
        init();
    }

    private void init() {
        mMemoryCache = new MemoryCache();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//        mWidth = dm.widthPixels / 4;
        mWidth = 100;

    }

    public void clearMemoryCache(){
        mMemoryCache.clear();
    }

    @Override
    protected void bindItemViewData(ViewHolder viewHolder, final FileItem item) {
        ImageView iv = viewHolder.getView(R.id.imagev_select_item_show_iv);
        Log.e("yjz", "width====>" + iv.getMeasuredWidth());
        Log.e("yjz", "height====>" + iv.getMeasuredHeight());


        if (item.actionType == FileItem.ACTION_TYPE_CAMERA) {
            iv.setImageResource(android.R.drawable.ic_menu_camera);
        } else {
            iv.setImageBitmap(mMemoryCache.getBitmapFromMemCache(item.path));
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, item.path, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_image_select_item;
    }

}
