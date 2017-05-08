package com.argandevteam.fpes.activity;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.user_photo_container)
    LinearLayout luserPhotoContainer;

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
    private String name, email, password;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        activity = this;
        setUpFirebase();

        loginLink.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        luserPhotoContainer.setOnClickListener(this);
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

    private void firebaseCreateAccount(final String userName, String email, String password) {
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
                            final User newUser = new User(task.getResult().getUser().getUid(), emailText.getText().toString(),
                                    nameText.getText().toString(), null);

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();

                            task.getResult().getUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDatabase.push().setValue(newUser, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                System.out.println("Data could not be saved " + databaseError.getMessage());

                                            } else {
                                                System.out.println("Data saved successfully.");
                                                Intent signUpIntent = new Intent(activity, MainActivity.class);
                                                startActivity(signUpIntent);
                                            }
                                        }
                                    });
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
                name = nameText.getText().toString();
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                if (validateInput(name, email, password)) {
                    firebaseCreateAccount(name, email, password);
                } else {
                    Toast.makeText(this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.link_login:
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.user_photo_container:
                setUserPhoto();
            default:
                break;
        }
    }

    private void setUserPhoto() {
        Toast.makeText(activity, "Te voy a coger", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInput(String userName, String email, String password) {
        boolean isValidInput = true;
        String errorMessage = null;
        if (TextUtils.isEmpty(userName)) {
            errorMessage = "Introduzca nombre de usuario";
            isValidInput = false;
        }
        toggleTextInputLayoutError(nameLayout, errorMessage);

        if (TextUtils.isEmpty(email)) {
            errorMessage = "Introduzca email";
            isValidInput = false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Email no valido";
            isValidInput = false;

        }
        toggleTextInputLayoutError(emailLayout, errorMessage);

        if (TextUtils.isEmpty(password)) {
            errorMessage = "Introduzca contraseña";
            isValidInput = false;
        } else if (password.length() < 6) {
            errorMessage = "Minimo 6 caracteres";
            isValidInput = false;
        }
        toggleTextInputLayoutError(passwordLayout, errorMessage);

        clearFocus();
        return isValidInput;
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
