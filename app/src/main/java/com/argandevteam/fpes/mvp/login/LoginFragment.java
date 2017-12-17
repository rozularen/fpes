package com.argandevteam.fpes.mvp.login;


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
import com.argandevteam.fpes.mvp.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements LoginContract.View, View.OnClickListener {


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
            case R.id.login_button:
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                presenter.doLoginWithEmailAndPassword(email, password);
                break;
            case R.id.google_login_button:
                presenter.doLoginWithGoogle();
                break;
            case R.id.fb_login_button:
                presenter.doLoginWithFacebook();
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
    public void navigateToHome() {
        mainActivity.navigateToHome();
    }

    @Override
    public void showFirebaseLoginFailed() {
        Toast.makeText(getContext(), "Login with Firebase failed!", Toast.LENGTH_SHORT).show();
    }
}
