package com.argandevteam.fpes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by markc on 30/03/2017.
 */

public class ReviewsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Review> reviews;

    public ReviewsAdapter(Context context, ArrayList<Review> myList) {
        this.context = context;
        this.reviews = myList;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
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
                    inflate(R.layout.review_item, parent, false);
        }
        ReviewsAdapter.ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(convertView);
        // get current item to be displayed
        Review currentItem = (Review) getItem(position);
        viewHolder.userName.setText("Marcos Stival");
//        viewHolder.userReview.setText(currentItem.text);

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.user_icon)
        ImageView userIcon;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.user_review)
        TextView userReview;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
