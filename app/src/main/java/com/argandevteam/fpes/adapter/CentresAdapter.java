package com.argandevteam.fpes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.model.Centre;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by markc on 24/11/2016.
 */

public class CentresAdapter extends BaseAdapter {

    private Context context; //context
    private ArrayList<Centre> items; //data source of the list adapter

    //public constructor
    public CentresAdapter(Context context, ArrayList<Centre> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);

        // get current item to be displayed
        Centre currentItem = (Centre) getItem(position);

        viewHolder.specificDenText.setText(currentItem.specific_den);
        viewHolder.provinceText.setText(currentItem.province);
        // returns the view for the current row
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tSpecificDenomination)
        TextView specificDenText;
        @BindView(R.id.tProvince)
        TextView provinceText;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}