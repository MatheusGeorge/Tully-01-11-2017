package com.example.edu.menutb.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.LoginController;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.view.MainActivity;
import com.example.edu.menutb.view.register.RegisterActivity;

/**
 * A login screen that offers login via email/password.
 *  implements LoaderManager.LoaderCallbacks<Cursor>
 */
public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    //private View mProgressView;
    private View mLoginFormView;
    private TextView textViewLoginError;
    LinearLayout base;
    String tokenString;
    private ProgressDialog progressBar;
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        SharedPreferences token = this.getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
        if(!tokenString.isEmpty()){
            // aparecer a progress bar
            progressBar = new ProgressDialog(LoginActivity.this);
            progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressBar.setIndeterminate(true);
            progressBar.setCancelable(true);
            progressBar.show();
            progressBar.setContentView(R.layout.progress_bar);
            // fim
            new ValidTokenTask().execute();
        }
        // Set up the login form.
        mUserView = (AutoCompleteTextView) findViewById(R.id.user);

        //Decomentar caso o banco precise de um alter table
        //this.deleteDatabase("Tully.db");
        textViewLoginError = (TextView) findViewById(R.id.textViewErrorLogin);
        textViewLoginError.setVisibility(View.INVISIBLE);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        base = (LinearLayout) findViewById(R.id.linearLayoutLogin);
        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(base.getWindowToken(), 0);

            }
        });
        Button mSignInButton = (Button) findViewById(R.id.buttonSignIn);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mForgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        mForgotPassword.setOnClickListener(listenerPassword);
        mLoginFormView = findViewById(R.id.loginForm);
        //mProgressView = findViewById(R.id.loginProgress);

        Button mSingUpButton = (Button) findViewById(R.id.buttonSignUp);
        mSingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ValidTokenTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                return new LoginController().isTokenValid(tokenString);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private final View.OnClickListener listenerPassword = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RecoveryPasswordActivity.class);
            startActivity(intent);
        }
    };

    private void attemptLogin() {

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        mAuthTask = null;

        if (!user.equals("admin")) {
            if (mAuthTask != null) {
                return;
            }

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.errorFieldRequired));
                focusView = mPasswordView;
                cancel = true;
            } else if (!(password.length() > 7)){
                mPasswordView.setError(getString(R.string.errorInvalidPassword));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(user)) {
                mUserView.setError(getString(R.string.errorFieldRequired));
                focusView = mUserView;
                cancel = true;
            } else if (user.length() < 3) {
                mUserView.setError(getString(R.string.errorInvalidUser));
                focusView = mUserView;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                mAuthTask = new UserLoginTask(user, password);
                mAuthTask.execute();
                Snackbar.make(mLoginFormView, R.string.loginAccept, Snackbar.LENGTH_INDEFINITE)
                        .setAction("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
            }

        } else {
            Snackbar.make(mLoginFormView, R.string.loginErrorAdmin, Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
        }

    }

    private class UserLoginTask extends AsyncTask<String, Void, UserTully> {

        private final String mUsuario;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mUsuario = email;
            mPassword = password;
        }

        protected UserTully doInBackground(String... params) {
            try {
                return new LoginController().attemptLogin(mUsuario, mPassword);
            } catch (Exception e) {
                Snackbar.make(mLoginFormView, getString(R.string.connectionError), Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }

        protected void onPostExecute(UserTully userTully) {
            if (userTully == null) {
                textViewLoginError.setVisibility(View.VISIBLE);
                mPasswordView.setText("");
                mUserView.setText("");
                final android.os.Handler delay = new android.os.Handler();
                delay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textViewLoginError.setVisibility(View.INVISIBLE);
                    }
                }, 10000);
            } else {
                createUserData(userTully);
                preferenceId(userTully);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        private void preferenceId(UserTully userTully) {
            // gravar Preferencia
            SharedPreferences idGrava = getSharedPreferences("id", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorId = idGrava.edit();
            SharedPreferences tokenGrava = getSharedPreferences("token", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorToken = tokenGrava.edit();
            editorId.putString("id", userTully.getId());
            editorToken.putString("token", userTully.getToken());
            editorId.apply();
            editorToken.apply();

        }

        private void createUserData(final UserTully userTully) {
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    new LoginController().insertUserLogin(LoginActivity.this, userTully);
                    return null;
                }
            };
            task.execute();
        }
    }
}

