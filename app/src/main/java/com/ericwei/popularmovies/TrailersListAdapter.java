package com.ericwei.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ericwei on 2017-01-31.
 */

public class TrailersListAdapter extends BaseAdapter {

    private static LayoutInflater inflater;
    private Context mContext;
    private String[] mTrailerDataArray;

    public TrailersListAdapter(Context mContext, String[] mTrailerDataArray) {
        this.mTrailerDataArray = mTrailerDataArray;
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mTrailerDataArray.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return view;
    }
}
