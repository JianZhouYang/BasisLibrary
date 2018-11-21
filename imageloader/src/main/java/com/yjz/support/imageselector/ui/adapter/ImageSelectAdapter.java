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
import java.util.List;

public class ImageSelectAdapter extends RecyclerDefaultAdapter<FileItem> {
    private int mWidth;

    public ImageSelectAdapter(Context context) {
        super(context);
        init();
    }

    public ImageSelectAdapter(Context context, List<FileItem> list) {
        super(context, list);
        init();
    }

    private void init() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//        mWidth = dm.widthPixels / 4;
        mWidth = 100;
    }

    @Override
    protected void bindItemViewData(ViewHolder viewHolder, final FileItem item) {
        ImageView iv = viewHolder.getView(R.id.imagev_select_item_show_iv);
        Log.e("yjz", "width====>" + iv.getMeasuredWidth());
        Log.e("yjz", "height====>" + iv.getMeasuredHeight());

        Bitmap bitmap = (Bitmap) iv.getTag();
        if (null != bitmap) {
            bitmap.recycle();
        }

        if (item.actionType == FileItem.ACTION_TYPE_CAMERA) {
            iv.setImageResource(android.R.drawable.ic_menu_camera);
        } else {

//            bitmap = BitmapFactory.decodeFile(item.path, options);
            bitmap = compressScaleBitmap(item.path);
            iv.setTag(bitmap);
            iv.setImageBitmap(bitmap);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, item.path, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap compressScaleBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int toWidth = mWidth;
        int toHeight = options.outHeight * toWidth / options.outWidth;
        options.inSampleSize = 6;
        options.outWidth = toWidth;
        options.outHeight = toHeight;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_image_select_item;
    }

}
