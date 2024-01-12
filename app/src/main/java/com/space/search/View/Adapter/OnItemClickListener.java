package com.space.search.View.Adapter;

import android.view.View;

import com.space.search.Model.Response;

public interface OnItemClickListener {

    void onClick(Response.Item item, View view);
}
