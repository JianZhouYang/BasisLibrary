package com.yjz.support.imageselector.ui.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.yjz.support.R;
import com.yjz.support.imageselector.base.FileItem;
import com.yjz.support.imageselector.adapterbase.RecyclerDefaultAdapter;

import java.util.List;

public class ImageSelectAdapter extends RecyclerDefaultAdapter<FileItem> {
    public ImageSelectAdapter(Context context) {
        super(context);
    }

    public ImageSelectAdapter(Context context, List<FileItem> list) {
        super(context, list);
    }

    @Override
    protected void bindItemViewData(ViewHolder viewHolder, FileItem item) {
        ImageView iv = viewHolder.getView(R.id.imagev_select_item_show_iv);
        iv.setImageBitmap(BitmapFactory.decodeFile(item.path));
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_image_select_item;
    }

}
