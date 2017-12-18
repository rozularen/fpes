package com.argandevteam.fpes;

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

import com.argandevteam.fpes.map.MapFragment;
import com.argandevteam.fpes.data.Centre;
import com.argandevteam.fpes.centres.ListFragment;
import com.argandevteam.fpes.centres.ListPresenter;
import com.argandevteam.fpes.login.LoginFragment;
import com.argandevteam.fpes.login.LoginPresenter;
import com.argandevteam.fpes.login.facebook.FacebookSignIn;
import com.argandevteam.fpes.login.google.GoogleSignIn;
import com.argandevteam.fpes.profile.ProfileFragment;
import com.argandevteam.fpes.register.RegisterFragment;
import com.argandevteam.fpes.register.RegisterPresenter;
import com.argandevteam.fpes.ui.CustomLinearLayout;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements ListFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final int GET_FROM_GALLERY = 1;


    CustomLinearLayout customLinearLayout;
    TextView drawerHeaderName;
    ImageView drawerUserPhoto;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation)
    NavigationView mDrawerList;

    @BindView(R.id.container)
    FrameLayout frameLayout;

    ProfileFragment profileFragment;
    MapFragment mapFragment;

    private RegisterPresenter registerPresenter;
    private RegisterFragment registerFragment;

    private LoginPresenter loginPresenter;
    private LoginFragment loginFragment;
    private GoogleSignIn googleSignInPresenter;

    private FacebookSignIn facebookSignInPresenter;
    private ListPresenter listPresenter;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MobileAds.initialize(this, "ca-app-pub-5632055827237755~3780528429");

        // Set up Toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        View header = mDrawerList.getHeaderView(0);
        customLinearLayout = (CustomLinearLayout) header.findViewById(R.id.custom_linear_layout);
        drawerHeaderName = (TextView) header.findViewById(R.id.drawer_header_name);
        drawerUserPhoto = (ImageView) header.findViewById(R.id.drawer_user_icon);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_closed);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        mDrawerList.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                });
        setDrawerState(false);

        // Start
        loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, loginFragment, loginFragment.getTag())
                    .commit();
        }

        loginPresenter = new LoginPresenter(loginFragment);

        googleSignInPresenter = new GoogleSignIn(this, loginPresenter);
        facebookSignInPresenter = new FacebookSignIn(this, loginPresenter);

        googleSignInPresenter.createGoogleClient();

        loginPresenter.setGoogleSignInPresenter(googleSignInPresenter);
        loginPresenter.setFacebookSignInPresenter(facebookSignInPresenter);
        // Done
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass = null;

        if (menuItem.getItemId() == R.id.nav_log_out) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Cerrar sesion")
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            navigateToLogin();
                            loginPresenter.logOff();
                            googleSignInPresenter.logOff();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
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
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.getTag())
                        .commit();
                setTitle(menuItem.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
        if (requestCode == GET_FROM_GALLERY && resultCode == -1) {
            profileFragment.onActivityResult(requestCode, resultCode, data);
        }
        loginPresenter.onResult(requestCode, resultCode, data);
    }

    private void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.syncState();
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.syncState();
        }
    }

    //Navigation methods
    public void navigateToHome() {
        ListFragment listFragment = ListFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, listFragment)
                .commit();

        listPresenter = new ListPresenter(listFragment);

        //Set up drawer menu
        setDrawerState(true);
    }

    public void navigateToLogin() {
        LoginFragment loginFragment = LoginFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loginFragment)
                .commit();

        loginPresenter = new LoginPresenter(loginFragment);

        //This smells bad
        loginPresenter.setGoogleSignInPresenter(googleSignInPresenter);
        loginPresenter.setFacebookSignInPresenter(facebookSignInPresenter);

        setDrawerState(false);
    }

    public void navigateToRegister() {
        registerFragment = RegisterFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, registerFragment)
                .addToBackStack(null)
                .commit();

        registerPresenter = new RegisterPresenter(registerFragment);
        setDrawerState(false);
    }
}
