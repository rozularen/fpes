package com.argandevteam.fpes.activity;

import android.app.SearchManager;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.CustomLinearLayout;
import com.argandevteam.fpes.R;
import com.argandevteam.fpes.fragment.CentreFragment;
import com.argandevteam.fpes.fragment.MapFragment;
import com.argandevteam.fpes.fragment.ProfileFragment;
import com.argandevteam.fpes.model.Centre;
import com.argandevteam.fpes.utils.CircleTransform;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class MainActivity extends AppCompatActivity implements CentreFragment.OnListFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private MainActivity activity;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    CustomLinearLayout customLinearLayout;
    TextView drawerHeaderName;
    ImageView drawerUserPhoto;

    @BindView(R.id.navigation)
    NavigationView mDrawerList;

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        ButterKnife.bind(this);

        activity = this;
        View header = mDrawerList.getHeaderView(0);
        customLinearLayout = (CustomLinearLayout) header.findViewById(R.id.custom_linear_layout);
        drawerHeaderName = (TextView) header.findViewById(R.id.drawer_header_name);
        drawerUserPhoto = (ImageView) header.findViewById(R.id.drawer_user_icon);

        setUpFirebase();
        setUpGoogleSignIn();

        setSupportActionBar(mToolbar);

        mToolbar.setTitle(R.string.app_name);
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    drawerHeaderName.setText(currentUser.getDisplayName());
                    Picasso.with(activity).load(currentUser.getPhotoUrl()).fit().transform(new CircleTransform()).into(drawerUserPhoto);
                    Picasso.with(activity).load(currentUser.getPhotoUrl()).transform(new BlurTransformation(activity)).into(customLinearLayout);
                } else {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.drawer_open, R.string.drawer_closed);
        // Set the list's click listener
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new CentreFragment()).commit();

        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });

    }

    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(MainActivity.this, "User: " + user.getEmail() + " is logged in.", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Toast.makeText(MainActivity.this, "User logged out.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    private void setUpGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("64500376949-1erlve8i66osmdfq6l6kc3p4e7igivhg.apps.googleusercontent.com")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass = null;

        if (menuItem.getItemId() == R.id.nav_log_out) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Cerrar sesion").setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    LoginManager.getInstance().logOut();

                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        } else if (menuItem.getItemId() == R.id.nav_map) {
            fragmentClass = MapFragment.class;
        } else if (menuItem.getItemId() == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
        } else {
            fragmentClass = CentreFragment.class;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                setTitle(menuItem.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "handleIntent: " + query);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }


    @Override
    public void onListFragmentInteraction(Centre item) {
        Log.d(TAG, "onListFragmentInteraction: asdasdasd");
    }
}
