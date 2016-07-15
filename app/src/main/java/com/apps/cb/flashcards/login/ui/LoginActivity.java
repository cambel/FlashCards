package com.apps.cb.flashcards.login.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.apps.cb.flashcards.FlashCardsApp;
import com.apps.cb.flashcards.R;
import com.apps.cb.flashcards.login.LoginPresenter;
import com.apps.cb.flashcards.main.ui.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @Bind(R.id.btnLogin)
    LoginButton loginButton;
    @Bind(R.id.loginContainer)
    RelativeLayout loginContainer;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private CallbackManager callbackManager;
    @Inject
    LoginPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        setupInjection();

        presenter.onCreate();

        if (AccessToken.getCurrentAccessToken() != null) {
            presenter.login(AccessToken.getCurrentAccessToken());
        }

        setupFacebookLogin();

    }

    private void setupInjection() {
        FlashCardsApp app = (FlashCardsApp) getApplication();
        app.getLoginComponent(this, this).inject(this);
    }


    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.login(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Snackbar.make(loginContainer, R.string.login_cancel_error, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                loginError(exception.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void handleSignIn() {
        navigateToMainScreen();
    }

    @Override
    public void loginError(String error) {
        String msgError = String.format(getString(R.string.login_error), error != null ? error : "");
        Snackbar.make(loginContainer, msgError, Snackbar.LENGTH_SHORT).show();
    }

    private void getUserInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.i("FacebookRequest", response.toString());
                        try {
                            if (object != null) {
                                Log.d("FacebookRequest", "name: " + object.getString("name"));
                                Log.d("FacebookRequest", "email: " + object.getString("email"));
                                setUserEmail(object.getString("name"));
                            }
                        } catch (JSONException e) {
                            Log.d("FacebookRequest", "something went wrong :/");
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void setUserEmail(String email) {
        if (email != null) {
            sharedPreferences.edit().putString(FlashCardsApp.EMAIL_KEY, email).apply();
        }else{
            getUserInfo(AccessToken.getCurrentAccessToken());
        }
    }

    @Override
    public void hideUIElements() {
        loginButton.setVisibility(View.GONE);
    }
    @Override
    public void showUIElements() {
        loginButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}
