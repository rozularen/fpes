package com.argandevteam.fpes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.model.User;
import com.argandevteam.fpes.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private LoginManager mLoginManager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private AuthCredential fbCredential;
    private AuthCredential googleCredential;

    private static final int RC_SIGN_IN_GOOGLE = 1;

    CallbackManager mCallbackManager;

    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.input_email)
    TextInputEditText emailText;
    @BindView(R.id.input_password)
    TextInputEditText passwordText;

    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.google_login_button)
    Button googleLoginButton;
    @BindView(R.id.fb_login_button)
    Button fbLogin;

    @BindView(R.id.link_signup)
    TextView signupLink;
    private SharedPreferences sharedPreferences;

//    @BindView(R.id.sign_in_button)
//    SignInButton googleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences(Constants.ApplicationTag, Activity.MODE_PRIVATE);


        mAuth = FirebaseAuth.getInstance();
        mLoginManager = LoginManager.getInstance();

        setUpGoogleSignIn();

        signupLink.setOnClickListener(this);
        googleLoginButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        fbLogin.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("users");

        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        fbCredential = getCredential(loginResult.getAccessToken());
                        signInWithCredentials(fbCredential);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(LoginActivity.this, "prueba" + exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                    finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };
    }


    private AuthCredential getCredential(AccessToken accessToken) {
        return FacebookAuthProvider.getCredential(accessToken.getToken());
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

    private void signInWithCredentials(final AuthCredential credential) {
        Log.d(TAG, "handleFirebaseAuthWithProvider:" + credential.getProvider());

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if (!sharedPreferences.contains(Constants.FIRST_LAUNCH)) {
                        addUser(task.getResult().getUser());
                        sharedPreferences.edit().putBoolean(Constants.FIRST_LAUNCH, true).apply();
                    }
                }
            }
        });
    }

    private void addUser(FirebaseUser user) {
        User newUser = new User(user.getUid(), user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString());
        userRef.child(user.getUid()).setValue(newUser);
    }

    private void signInEmailAndPassword(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Nopeee",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            if (!sharedPreferences.contains(Constants.FIRST_LAUNCH)) {
                                addUser(task.getResult().getUser());
                                sharedPreferences.edit().putBoolean(Constants.FIRST_LAUNCH, true).apply();
                            }
                        }
                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                googleCredential = getCredential(acct);
                signInWithCredentials(googleCredential);
            } else {

                Log.d("Google_Sign_In", "Login ");
            }
        }

    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    private AuthCredential getCredential(GoogleSignInAccount acct) {
        return GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_signup:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
//            case R.id.sign_in_button:
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent, 1);
//                break;
            case R.id.login_button:
                if (validateInput(emailText.getText().toString(), passwordText.getText().toString())) {
                    signInEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString());
                }
                break;
            case R.id.fb_login_button:
                mLoginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.google_login_button:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 1);
            default:
                break;
        }
    }

    private boolean validateInput(String email, String password) {
        boolean isValidInput = true;
        String errorMessage = null;
        if (TextUtils.isEmpty(email)) {
            errorMessage = "Introduzca email";
            isValidInput = false;
        }
        setError(emailInputLayout, errorMessage);
        if (TextUtils.isEmpty(password)) {
            errorMessage = "Introduzca contraseña";
            isValidInput = false;
        }
        setError(passwordInputLayout, errorMessage);
        clearFocus();
        return isValidInput;
    }

    private void setError(@NonNull TextInputLayout textInputLayout,
                          String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}
