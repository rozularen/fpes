package com.argandevteam.fpes.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.name_input_layout)
    TextInputLayout nameLayout;
    @BindView(R.id.email_input_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordLayout;

    @BindView(R.id.ainput_email)
    TextInputEditText emailText;
    @BindView(R.id.ainput_password)
    TextInputEditText passwordText;
    @BindView(R.id.ainput_name)
    TextInputEditText nameText;

    @BindView(R.id.link_login)
    TextView loginLink;

    @BindView(R.id.signup_button)
    Button signUpButton;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setUpFirebase();

        loginLink.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    ;

    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(RegisterActivity.this, "User: " + user.getEmail() + " is logged in.", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Toast.makeText(RegisterActivity.this, "User logged out.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ..
            }
        };
    }

    private void firebaseCreateAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.d("Gasdasda", task.getException().getMessage());

                            Toast.makeText(RegisterActivity.this, "fallo",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            User newUser = new User(task.getResult().getUser().getUid(), emailText.getText().toString(),
                                    nameText.getText().toString(), task.getResult().getUser().getPhotoUrl().toString());
                            mDatabase.push().setValue(newUser, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        System.out.println("Data could not be saved " + databaseError.getMessage());
                                    } else {
                                        System.out.println("Data saved successfully.");
                                    }
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:
                validateInput(nameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString());
                break;
            case R.id.link_login:
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            default:
                break;
        }
    }

    private void validateInput(String userName, String email, String password) {
        String errorMessage = null;
        if (TextUtils.isEmpty(userName)) {
            errorMessage = "Introduzca nombre de usuario";
        }
        toggleTextInputLayoutError(nameLayout, errorMessage);

        if (TextUtils.isEmpty(email)) {
            errorMessage = "Introduzca email";
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Email no valido";
        }
        toggleTextInputLayoutError(emailLayout, errorMessage);

        if (TextUtils.isEmpty(password)) {
            errorMessage = "Introduzca contraseña";
        } else if (password.length() < 6) {
            errorMessage = "Minimo 6 caracteres";
        }
        toggleTextInputLayoutError(passwordLayout, errorMessage);

        clearFocus();

    }

    /**
     * Display/hides TextInputLayout error.
     *
     * @param msg the message, or null to hide
     */
    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
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
