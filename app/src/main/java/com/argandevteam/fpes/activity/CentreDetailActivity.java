package com.argandevteam.fpes.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.adapter.ReviewsAdapter;
import com.argandevteam.fpes.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CentreDetailActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.lvReviews)
    ListView lvReviews;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

    ReviewsAdapter reviewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_detail);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Detalles");
        Picasso.with(this)
                .load("https://i2.wp.com/extracine.com/files/2012/07/utad-las-rozas-madrid.jpeg?resize=1160%2C830").into(imageView);

        ArrayList<Review> myList = new ArrayList<>();
        myList.add(new Review(null, 4f, "HOLA"));
        reviewsAdapter = new ReviewsAdapter(this, myList);
        lvReviews.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
