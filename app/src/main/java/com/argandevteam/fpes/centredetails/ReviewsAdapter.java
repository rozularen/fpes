package com.argandevteam.fpes.centredetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.data.Review;
import com.argandevteam.fpes.data.User;
import com.argandevteam.fpes.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by markc on 30/03/2017.
 */

public class ReviewsAdapter extends BaseAdapter implements View.OnClickListener {

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
        if (review != null) {
            User user = null;
            if (review.user != null) {
                user = review.user;
                holder.reviewUserName.setText(user.name);
                Picasso.with(context)
                        .load(review.user.user_image)
                        .fit()
                        .transform(new CircleTransform())
                        .into(holder.reviewUserIcon);
            }

            holder.reviewText.setText(review.text);
            holder.reviewRating.setRating(review.rating);
            holder.reviewDate.setText(review.date);
            holder.reviewLikeButton.setOnClickListener(this);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_review_likes:
                incrementReviewLike();
                break;
            default:
                break;
        }
    }

    private void incrementReviewLike() {

    }

    static class ViewHolder {

        @BindView(R.id.image_user_image)
        ImageView reviewUserIcon;
        @BindView(R.id.text_review_username)
        TextView reviewUserName;
        @BindView(R.id.rb_user_rating)
        RatingBar reviewRating;
        @BindView(R.id.text_review_text)
        TextView reviewText;
        @BindView(R.id.text_review_date)
        TextView reviewDate;
        @BindView(R.id.btn_review_likes)
        ImageButton reviewLikeButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
