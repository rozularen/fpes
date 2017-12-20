package com.argandevteam.fpes.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment
        implements LoginContract.View, View.OnClickListener {

    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;

    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.et_email)
    TextInputEditText emailText;

    @BindView(R.id.et_password)
    TextInputEditText passwordText;

    @BindView(R.id.btn_log_in)
    Button loginButton;

    @BindView(R.id.btn_google_log_in)
    Button googleLoginButton;

    @BindView(R.id.btn_facebook_log_in)
    Button fbLogin;

    @BindView(R.id.text_register_link)
    TextView signupLink;

    private LoginContract.Presenter presenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        fbLogin.setOnClickListener(this);
        googleLoginButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        signupLink.setOnClickListener(this);

        return view;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
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
    public void onResume() {
        super.onResume();
        presenter.start();
        presenter.setAuthListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeAuthListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_in:
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                presenter.doLoginWithEmailAndPassword(email, password);
                break;
            case R.id.btn_google_log_in:
                presenter.doLoginWithGoogle();
                break;
            case R.id.btn_facebook_log_in:
                presenter.doLoginWithFacebook();
                break;
            case R.id.text_register_link:
                navigateToRegister();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onResult(requestCode, resultCode, data);
    }

    @Override
    public void navigateToRegister() {
        mainActivity.navigateToRegister();
    }

    @Override
    public void navigateToHome() {
        mainActivity.navigateToHome();
    }

    @Override
    public void showFirebaseLoginFailed() {
        Toast.makeText(getContext(), "Login with Firebase failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordError() {
        passwordInputLayout.setError("Password is null");
    }

    @Override
    public void setEmailError() {
        emailInputLayout.setError("Email is null");
    }

    @Override
    public void loadUserInfo(FirebaseUser firebaseUser) {
        mainActivity.loadUserInfo(firebaseUser);
    }
}
