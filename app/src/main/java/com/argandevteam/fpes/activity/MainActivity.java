package com.argandevteam.fpes.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.fragment.MapFragment;
import com.argandevteam.fpes.mvp.data.Centre;
import com.argandevteam.fpes.mvp.list.ListFragment;
import com.argandevteam.fpes.mvp.list.ListPresenter;
import com.argandevteam.fpes.mvp.login.LoginFragment;
import com.argandevteam.fpes.mvp.login.LoginPresenter;
import com.argandevteam.fpes.mvp.profile.ProfileFragment;
import com.argandevteam.fpes.ui.CustomLinearLayout;
import com.facebook.CallbackManager;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    private static final int GET_FROM_GALLERY = 1;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    CustomLinearLayout customLinearLayout;
    TextView drawerHeaderName;
    ImageView drawerUserPhoto;
    @BindView(R.id.navigation)
    NavigationView mDrawerList;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    FrameLayout frameLayout;
    ProfileFragment profileFragment;
    MapFragment mapFragment;

    private MainActivity activity;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        ButterKnife.bind(this);

        MobileAds.initialize(this, "ca-app-pub-5632055827237755~3780528429");
        activity = this;

        View header = mDrawerList.getHeaderView(0);
        customLinearLayout = (CustomLinearLayout) header.findViewById(R.id.custom_linear_layout);
        drawerHeaderName = (TextView) header.findViewById(R.id.drawer_header_name);
        drawerUserPhoto = (ImageView) header.findViewById(R.id.drawer_user_icon);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.drawer_open, R.string.drawer_closed);
        // Set the list's click listener
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });


        // Start
        LoginFragment loginFragment = new LoginFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loginFragment, loginFragment.getTag())
                .commit();

        mLoginPresenter = new LoginPresenter(loginFragment);

        // Done

    }

    private void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass = null;

        if (menuItem.getItemId() == R.id.nav_log_out) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Cerrar sesion").setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLoginPresenter.signOff();

                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            navigateToLogin();

        } else if (menuItem.getItemId() == R.id.nav_map) {
            fragmentClass = MapFragment.class;
            if (mapFragment != null) {
                mapFragment = new MapFragment();
            }
        } else if (menuItem.getItemId() == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
            if (profileFragment != null) {
                profileFragment = new ProfileFragment();
            }
        } else {
            fragmentClass = ListFragment.class;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.getTag()).commit();
                setTitle(menuItem.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ACTIVITY OK");
        new MapFragment().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onListFragmentInteraction(Centre item) {
        Log.d(TAG, "onListFragmentInteraction: asdasdasd");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == -1) {
            profileFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Navigation methods

    public void navigateToHome() {
        ListFragment listFragment = new ListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, listFragment)
                .commit();

        ListPresenter listPresenter = new ListPresenter(listFragment);
    }

    public void navigateToLogin() {
        LoginFragment loginFragment = new LoginFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loginFragment)
                .commit();

        mLoginPresenter = new LoginPresenter(loginFragment);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
