package com.argandevteam.fpes.centres;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.argandevteam.fpes.BaseFragment;
import com.argandevteam.fpes.R;
import com.argandevteam.fpes.centredetails.CentreDetailsFragment;
import com.argandevteam.fpes.data.Centre;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
public class CentresFragment extends BaseFragment implements CentresContract.View {

    private static final String TAG = "CentresFragment";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.rv_centres)
    RecyclerView recyclerView;

    @BindView(R.id.ad_view)
    AdView mAdView;
    CentresAdapter mAdapter;
    private OnListFragmentInteractionListener mListener;
    private List<Centre> myList;
    private DatabaseReference mDatabase;
    private DatabaseReference centresReviewsRef;
    private DatabaseReference centresRef;

    private CentresContract.Presenter mPresenter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CentresFragment() {

    }

    public static CentresFragment newInstance() {
        return new CentresFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_centre_list, container, false);

        // Set the adapter
        ButterKnife.bind(this, view);

        myList = new ArrayList<>();
        setHasOptionsMenu(true);
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        mDatabase = instance.getReference();

        centresRef = mDatabase.child("centres");
        centresReviewsRef = mDatabase.child("centres-reviews");
        mDatabase.child("centres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot centreSnapshot : dataSnapshot.getChildren()) {
                    Centre centre = centreSnapshot.getValue(Centre.class);
                    myList.add(centre);
                }
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Context context = view.getContext();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new CentresAdapter(getActivity(), myList, mListener);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CentresAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                CentreDetailsFragment centreDetailsFragment = new CentreDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("centre", myList.get(position));

                centreDetailsFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, centreDetailsFragment, centreDetailsFragment.getTag())
                        .addToBackStack(null)
                        .commit();

                Log.d(TAG, "onClick: " + myList.get(position).specific_den);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            AlertDialog filterDialog = null;
            View filterView = LayoutInflater.from(getContext()).inflate(R.layout.filter_dialog, null);

            final RadioButton cbPrivate = (RadioButton) filterView.findViewById(R.id.r_btn_private);
            final RadioButton cbPublic = (RadioButton) filterView.findViewById(R.id.r_btn_public);

            filterDialog = new AlertDialog.Builder(getContext()).setTitle("Filtrar")
                    .setView(filterView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myList.clear();
                            Log.d(TAG, "onClick: ");
                            if (cbPrivate.isChecked()) {
                                Log.d(TAG, "onClick: cbPrivate is checked");
                                centresRef.orderByChild("nature")
                                        .equalTo("Centro Privado")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot data) {
                                                for (DataSnapshot dataSnapshot : data.getChildren()) {
                                                    Centre centre = dataSnapshot.getValue(Centre.class);
                                                    myList.add(centre);
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.d(TAG, "onCancelled: ");
                                            }
                                        });
                            }
                        }
                    })
                    .create();
            filterDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
    }

    @Override
    public void setPresenter(CentresContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public void showCentres(List<Centre> centreList) {
        mAdapter.replaceData(centreList);
    }

    @Override
    public void showLoadingCentresError() {
        Toast.makeText(getContext(), "Can't get Centres!", Toast.LENGTH_SHORT).show();
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

    public interface OnLogOffListener {
        void onLogOff();
    }
}
