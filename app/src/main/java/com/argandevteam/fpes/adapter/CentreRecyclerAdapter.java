package com.argandevteam.fpes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.fragment.CentreFragment.OnListFragmentInteractionListener;
import com.argandevteam.fpes.model.Centre;
import com.argandevteam.fpes.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Centre} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CentreRecyclerAdapter extends RecyclerView.Adapter<CentreRecyclerAdapter.ViewHolder> {
    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    private ItemClickListener listener;
    private static final String TAG = "RecyclerViewAdapter";
    private final List<Centre> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;
    private DatabaseReference mDatabase;
    private DatabaseReference centresReviewsRef;


    public CentreRecyclerAdapter(Context context, List<Centre> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        centresReviewsRef = mDatabase.child("centres-reviews");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.centre_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, listener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Centre centre = mValues.get(position);
        calculateCentreAverageRating(holder, centre);
        holder.centreSpecificDen.setText(centre.specific_den);
        holder.centreAddress.setText(centre.address);
        holder.centreNature.setText(centre.nature);
        holder.centreNumReviews.setText(centre.reviews != null ? String.valueOf(centre.reviews.size()) : "0");
        holder.centreRating.setRating(centre.rating_average);

        Picasso.with(context).load(centre.thumbnail_url).fit().placeholder(R.drawable.com_facebook_button_background).centerCrop().into(holder.centreImage);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mListener.onListFragmentInteraction(holder.centre);
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    private void calculateCentreAverageRating(final ViewHolder viewHolder, final Centre centre) {
//        List<String> reviewsKeys = new ArrayList<>();
//        for(Map.Entry<String, Boolean> entry : centre.reviews.entrySet()){
//            reviewsKeys.add(entry.getKey());
//        }
        centresReviewsRef.child(String.valueOf(centre.id - 1)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    centre.sum_ratings += review.rating;
                    centre.num_ratings++;
                }
                centre.rating_average = centre.sum_ratings / centre.num_ratings;
                viewHolder.centreRating.setRating(centre.rating_average);
                Log.d(TAG, "onDataChange: " + centre.rating_average);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        Centre centre;
        Target target;

        @BindView(R.id.centre_image)
        ImageView centreImage;

        @BindView(R.id.centre_specific_den)
        TextView centreSpecificDen;

        @BindView(R.id.centre_address)
        TextView centreAddress;

        @BindView(R.id.centre_rating)
        RatingBar centreRating;

        @BindView(R.id.centre_num_reviews)
        TextView centreNumReviews;

        @BindView(R.id.centre_nature)
        TextView centreNature;


        ItemClickListener mListener;

        public ViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mView = itemView;
            mListener = listener;

        }

//        public void setOnItemClickListener(ItemClickListener mListener) {
//            this.mListener = mListener;
//        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }


    }


}
