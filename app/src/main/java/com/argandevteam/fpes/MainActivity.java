package com.argandevteam.fpes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import com.argandevteam.fpes.centres.CentresFragment;
import com.argandevteam.fpes.centres.CentresPresenter;
import com.argandevteam.fpes.data.Centre;
import com.argandevteam.fpes.login.LoginFragment;
import com.argandevteam.fpes.login.LoginPresenter;
import com.argandevteam.fpes.login.facebook.FacebookSignIn;
import com.argandevteam.fpes.login.google.GoogleSignIn;
import com.argandevteam.fpes.map.MapFragment;
import com.argandevteam.fpes.map.MapPresenter;
import com.argandevteam.fpes.profile.ProfileFragment;
import com.argandevteam.fpes.profile.ProfilePresenter;
import com.argandevteam.fpes.register.RegisterFragment;
import com.argandevteam.fpes.register.RegisterPresenter;
import com.argandevteam.fpes.ui.CustomLinearLayout;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements CentresFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private static final int GET_FROM_GALLERY = 1;

    CustomLinearLayout customLinearLayout;
    TextView drawerHeaderName;
    ImageView drawerUserPhoto;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation)
    NavigationView drawerList;

    @BindView(R.id.container)
    FrameLayout frameLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private RegisterFragment registerFragment;
    private RegisterPresenter registerPresenter;

    private LoginFragment loginFragment;
    private LoginPresenter loginPresenter;
    private GoogleSignIn googleSignInPresenter;
    private FacebookSignIn facebookSignInPresenter;

    private CentresFragment centresFragment;
    private CentresPresenter centresPresenter;

    private ProfileFragment profileFragment;
    private ProfilePresenter profilePresenter;

    private MapFragment mapFragment;
    private MapPresenter mapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MobileAds.initialize(this, "ca-app-pub-5632055827237755~3780528429");

        // Set up Toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        View header = drawerList.getHeaderView(0);
        customLinearLayout = (CustomLinearLayout) header.findViewById(R.id.custom_linear_layout);
        drawerHeaderName = (TextView) header.findViewById(R.id.text_drawer_username);
        drawerUserPhoto = (ImageView) header.findViewById(R.id.image_drawer_user_image);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        drawerList.setNavigationItemSelectedListener(
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

            getSupportFragmentManager()
                    .beginTransaction()
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
        if (menuItem.getItemId() == R.id.nav_log_out) {
            showConfirmDialog();
        } else if (menuItem.getItemId() == R.id.nav_map) {
            navigateToMap();
        } else if (menuItem.getItemId() == R.id.nav_profile) {
            navigateToProfile();
        } else {
            navigateToHome();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void showConfirmDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
            profilePresenter.onResult(requestCode, resultCode, data);
        }
        loginPresenter.onResult(requestCode, resultCode, data);
    }

    private void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.syncState();
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.syncState();
        }
    }

    /**
     * Navigation methods
     */
    public void navigateToHome() {
        centresFragment = CentresFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, centresFragment)
                .commit();

        centresPresenter = new CentresPresenter(centresFragment);

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

    private void navigateToProfile() {
        profileFragment = ProfileFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, profileFragment)
                .addToBackStack(null)
                .commit();

        profilePresenter = new ProfilePresenter(profileFragment);

        setDrawerState(true);
    }

    private void navigateToMap() {
        mapFragment = MapFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mapFragment)
                .addToBackStack(null)
                .commit();

        mapPresenter = new MapPresenter(mapFragment);

        setDrawerState(true);
    }

    public void loadUserInfo(FirebaseUser firebaseUser) {
        String displayName = firebaseUser.getDisplayName();
        drawerHeaderName.setText(displayName);

    }
}
