package com.dds.messagelist.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public abstract class ViewHolder<DATA> extends RecyclerView.ViewHolder {

    public abstract void onBind(DATA data);

    public ViewHolder(View itemView) {
        super(itemView);
    }
}
