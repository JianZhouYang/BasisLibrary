package com.yjz.support.imageselector.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerBaseAdapter<T, VH extends RecyclerBaseAdapter.RecyclerBaseViewHolder> extends RecyclerView.Adapter<VH> {
    protected Context mContext;
    protected List<T> mSourceList;

    public RecyclerBaseAdapter(Context context){
        this(context, new ArrayList<T>());
    }

    public RecyclerBaseAdapter(Context context, List<T> list){
        mContext = context;
        mSourceList = list;
    }

    protected abstract void bindItemViewData(VH vh, T item);

    public void setList(List<T> list){
        if (null != list) {
            mSourceList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void resetList(List<T> list){
        mSourceList.clear();
        if (null != list) {
            mSourceList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        try{
            bindItemViewData(holder, mSourceList.get(position));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mSourceList.size();
    }

    public static abstract class RecyclerBaseViewHolder extends RecyclerView.ViewHolder{

        public RecyclerBaseViewHolder(View itemView) {
            super(itemView);
        }

        protected <T extends View> T findViewById(int id){
            return (T) itemView.findViewById(id);
        }
    }
}
