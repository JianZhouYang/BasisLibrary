package com.yjz.support.imageselector.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yjz.support.imageselector.ImageBean;

import java.util.List;

public class ImageSelectAdapter extends RecyclerDefaultAdapter<ImageBean> {
    public ImageSelectAdapter(Context context) {
        super(context);
    }

    public ImageSelectAdapter(Context context, List<ImageBean> list) {
        super(context, list);
    }

    @Override
    protected void bindItemViewData(ViewHolder viewHolder, ImageBean item) {
        TextView tv = viewHolder.getView(0);

    }

    @Override
    protected int getResourceId() {
        return 0;
    }

}
