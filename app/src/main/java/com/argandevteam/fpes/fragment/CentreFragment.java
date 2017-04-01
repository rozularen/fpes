package com.argandevteam.fpes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.adapter.MyCentreRecyclerViewAdapter;
import com.argandevteam.fpes.model.Centre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CentreFragment extends Fragment {

    private static final String TAG = "CentreFragment";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    @BindView(R.id.lvCentres)
    RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;
    private List<Centre> myList;
    private DatabaseReference mDatabase;
    MyCentreRecyclerViewAdapter myCentreRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CentreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firebase
        myList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("centres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot centreSnapshot : dataSnapshot.getChildren()) {
                    Centre centre = centreSnapshot.getValue(Centre.class);
                    myList.add(centre);
                    Log.d(TAG, "onDataChange: " + centreSnapshot.getValue());
                }
                myCentreRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_centre_list, container, false);

        // Set the adapter
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        myCentreRecyclerViewAdapter = new MyCentreRecyclerViewAdapter(myList, mListener);
        recyclerView.setAdapter(myCentreRecyclerViewAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Centre item);
    }
}
