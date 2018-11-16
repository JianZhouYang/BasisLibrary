package com.yjz.support.imageselector.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerDefaultAdapter<T> extends RecyclerBaseAdapter<T, RecyclerDefaultAdapter.ViewHolder> {


    public RecyclerDefaultAdapter(Context context) {
        super(context);
    }

    public RecyclerDefaultAdapter(Context context, List<T> list) {
        super(context, list);
    }

    protected abstract int getResourceId();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(getResourceId(), parent, false));
    }


    public static class ViewHolder extends RecyclerBaseAdapter.RecyclerBaseViewHolder {
        private SparseArray<View> mViews = new SparseArray<>();

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public <T extends View> T getView(int id){
            T view = (T) mViews.get(id);
            if (null == view) {
                view = findViewById(id);
                mViews.put(id, view);
            }
            return view;
        }
    }
}
