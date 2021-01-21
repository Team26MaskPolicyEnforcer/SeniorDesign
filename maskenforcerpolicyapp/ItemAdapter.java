package com.example.maskenforcerpolicyapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.*;
public class ItemAdapter extends BaseAdapter{
    LayoutInflater mInflater;
    ArrayList<Integer> masks;
    ArrayList<Float> percentages;
    ArrayList<Integer> totalPeople;
    public ItemAdapter(Context c, ArrayList<Integer> m, ArrayList<Integer> t, ArrayList<Float> perc){
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        masks = m;
        totalPeople = t;
        percentages = perc;
    }
    @Override
    public int getCount() {
        return masks.size();
    }
    @Override
    public Object getItem(int position) {
        return masks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.item_layout, null);
        TextView maskTextView = (TextView) v.findViewById(R.id.maskTextView);
        TextView totalTextView = (TextView) v.findViewById(R.id.totalTextView);
        TextView percentageTextView = (TextView) v.findViewById(R.id.PercentageTextView);

        maskTextView.setText(masks.get(position).toString());
        totalTextView.setText(totalPeople.get(position).toString());
        percentageTextView.setText(percentages.get(position).toString());
        return v;
    }
}
