package com.argandevteam.fpes.mvp.data.source.remote;

import com.argandevteam.fpes.mvp.data.Centre;
import com.argandevteam.fpes.mvp.data.source.CentresDataSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markc on 06/12/2017.
 */

public class CentresRemoteDataSource implements CentresDataSource {

    private FirebaseDatabase instance;
    private DatabaseReference mDatabase;
    private DatabaseReference mCentresReference;
    private DatabaseReference mCentreReference;
    private DatabaseReference mCentreReviewsReference;


    public CentresRemoteDataSource() {
        instance = FirebaseDatabase.getInstance();
        mDatabase = instance.getReference();
        mCentresReference = mDatabase.child("centres");
    }


    @Override
    public void getCentres(final LoadCentresCallback callback) {
        final List<Centre> centreList = new ArrayList<>();
        mCentresReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot centreSnapshot : dataSnapshot.getChildren()) {
                    Centre centre = centreSnapshot.getValue(Centre.class);
                    centreList.add(centre);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError();
            }
        });
        callback.onCentresLoaded(centreList);
    }

    @Override
    public void getCentre(int centreId, LoadCentreCallback callback) {
        mCentreReference = mDatabase.child(String.valueOf(centreId - 1));
        mCentreReviewsReference = mDatabase.child("centres-reviews").child(String.valueOf(centreId - 1));

    }
}
