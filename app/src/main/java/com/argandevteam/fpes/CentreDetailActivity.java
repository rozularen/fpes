package com.argandevteam.fpes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CentreDetailActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_detail);
        ButterKnife.bind(this);
        Picasso.with(this)
                .load("https://i2.wp.com/extracine.com/files/2012/07/utad-las-rozas-madrid.jpeg?resize=1160%2C830").into(imageView);
    }

}
