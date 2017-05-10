package com.lckiss.placechoice.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lckiss.placechoice.Model.Location;
import com.lckiss.placechoice.R;

import java.util.List;

/**
 * Created by root on 17-4-18.
 */

public class MyAdapter extends BaseAdapter {
    private List<Location> sourseDates;
    private Context context;
    private View rowLayoutView;

    public MyAdapter(Context context, List<Location> sourseDates) {
        this.sourseDates = sourseDates;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sourseDates.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        rowLayoutView = layoutInflater.inflate(R.layout.item, null);
        TextView locationName = (TextView) rowLayoutView.findViewById(R.id.location);
        locationName.setText(sourseDates.get(position).getName());
        return rowLayoutView;
    }
}
