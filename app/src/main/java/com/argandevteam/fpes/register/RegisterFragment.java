package com.argandevteam.fpes.register;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements RegisterContract.View {

    @BindView(R.id.et_username)
    TextInputEditText etUsername;

    @BindView(R.id.et_email)
    TextInputEditText etEmail;

    @BindView(R.id.et_password)
    TextInputEditText etPassword;

    private RegisterContract.Presenter presenter;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onStop() {
        presenter.stop();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClicked(View view) {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        presenter.registerUser(username, email, password);
    }

    @OnClick(R.id.text_login_link)
    public void onLoginLinkClicked(View view) {
        mainActivity.navigateToLogin();
    }

    @Override
    public void showPasswordError() {

    }

    @Override
    public void showEmailError() {

    }

    @Override
    public void showUsernameError() {

    }

    @Override
    public void userRegisterFailed() {

    }

    @Override
    public void navigateToHome() {
        mainActivity.navigateToHome();
    }
}
