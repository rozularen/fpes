package com.argandevteam.fpes.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "SearchFragment";
    private SearchView searchView;

    @BindView(R.id.seek_bar)
    SeekBar radiusSeekBar;

    @BindView(R.id.radius_text)
    TextView radiusText;

    @BindView(R.id.search_button)
    Button searchButton;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusText.setText(progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        searchButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        if(menuItem != null){
            Log.d(TAG, "onCreateOptionsMenu: not null");
            searchView = (SearchView) menuItem.getActionView();
            if(searchView != null){
                Log.d(TAG, "onCreateOptionsMenu: NOT null");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
        }
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == searchButton.getId()){
            Toast.makeText(getActivity(), "HOLA", Toast.LENGTH_SHORT).show();
            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            CentreFragment fragment = new CentreFragment();
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, fragment.getTag()).commit();
        }
    }
}
