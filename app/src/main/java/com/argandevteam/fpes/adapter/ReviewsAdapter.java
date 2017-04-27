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
import com.argandevteam.fpes.utils.CircleTransform;
import com.squareup.picasso.Picasso;

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
        ReviewsAdapter.ViewHolder holder = new ReviewsAdapter.ViewHolder(convertView);

        // get current item to be displayed
        Review review = (Review) getItem(position);
        holder.reviewText.setText(review.text);
        holder.reviewRating.setRating(review.rating);
        holder.reviewDate.setText(review.date);
        if(review.user != null) {
            Picasso.with(context).load(review.user.user_image)
                    .placeholder(R.drawable.com_facebook_button_background)
                    .fit()
                    .transform(new CircleTransform())
                    .into(holder.reviewUserIcon);
        }
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.review_user_icon)
        ImageView reviewUserIcon;
        @BindView(R.id.review_user_name)
        TextView reviewUserName;
        @BindView(R.id.review_rating)
        RatingBar reviewRating;
        @BindView(R.id.review_text)
        TextView reviewText;
        @BindView(R.id.review_date)
        TextView reviewDate;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
