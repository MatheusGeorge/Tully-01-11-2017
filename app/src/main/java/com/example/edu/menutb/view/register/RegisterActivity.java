package com.example.edu.menutb.view.register;

/**
 * Created by jeffkenichi on 8/18/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.RegisterController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    EditText nameRegister;
    EditText emailRegister;
    EditText userRegister;
    EditText passwordRegister;
    EditText confirmPasswordRegister;
    LinearLayout base;

    Button buttonNext;
    VerifyUserTask mVerifyUserTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_first);

        nameRegister = (EditText) findViewById(R.id.editTextNameRegister);
        emailRegister = (EditText) findViewById(R.id.editTextEmailRegister);
        userRegister = (EditText) findViewById(R.id.editTextUserRegister);
        passwordRegister = (EditText) findViewById(R.id.editTextPasswordRegister);
        confirmPasswordRegister = (EditText) findViewById(R.id.editTextConfirmPasswordRegister);
        base = (LinearLayout) findViewById(R.id.linearLayoutRegister);
        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(base.getWindowToken(), 0);

            }
        });
        confirmPasswordRegister.setImeOptions(EditorInfo.IME_ACTION_DONE);
        buttonNext = (Button) findViewById(R.id.buttonNextRegister);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFields();
            }
        });
    }

    private void saveData() {
        Intent intent = new Intent(getApplicationContext(), RegisterPhotoActivity.class);
        intent.putExtra("nameRegister", nameRegister.getText().toString());
        intent.putExtra("emailRegister", emailRegister.getText().toString());
        intent.putExtra("userRegister", userRegister.getText().toString());
        intent.putExtra("passwordRegister", passwordRegister.getText().toString());
        intent.putExtra("confirmPasswordRegister", confirmPasswordRegister.getText().toString());
        startActivity(intent);
    }


    private boolean isNameValid(String name) {
        return name.length() >= 3;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isUserValid(String user) {
        return user.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private void verifyFields() {
        String name = nameRegister.getText().toString();
        String email = emailRegister.getText().toString();
        String user = userRegister.getText().toString();
        String password = passwordRegister.getText().toString();
        String passwordConfirm = confirmPasswordRegister.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(name)) {
            nameRegister.setError(getString(R.string.errorFieldRequired));
            focusView = nameRegister;
            cancel = true;
        } else if (!isNameValid(name)) {
            nameRegister.setError(getString(R.string.errorNameInvalid));
            focusView = nameRegister;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailRegister.setError(getString(R.string.errorFieldRequired));
            focusView = emailRegister;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailRegister.setError(getString(R.string.errorEmailInvalid));
            focusView = emailRegister;
            cancel = true;
        }
        // Check for a valid user
        if (TextUtils.isEmpty(user)) {
            userRegister.setError(getString(R.string.errorFieldRequired));
            focusView = userRegister;
            cancel = true;
        } else if (!isUserValid(user)) {
            userRegister.setError(getString(R.string.errorInvalidUser));
            focusView = userRegister;
            cancel = true;
        }
        //Check for a valid password
        if (TextUtils.isEmpty(password)) {
            passwordRegister.setError(getString(R.string.errorFieldRequired));
            focusView = passwordRegister;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordRegister.setError(getText(R.string.changePasswordInvalid));
        }
        //Check if the confirmPassword is equals
        if (TextUtils.isEmpty(passwordConfirm)) {
            confirmPasswordRegister.setError(getString(R.string.errorFieldRequired));
            focusView = confirmPasswordRegister;
            cancel = true;
        } else if (!isConfirmPasswordValid(password, passwordConfirm)) {
            confirmPasswordRegister.setError(getString(R.string.errorConfirmPasswordInvalid));
            focusView = confirmPasswordRegister;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mVerifyUserTask = new VerifyUserTask(user, email);
            mVerifyUserTask.execute();
        }
    }

    private class VerifyUserTask extends AsyncTask<String, Void, Integer> {

        private final String mUser;
        private final String mEmail;


        VerifyUserTask(String user, String email) {
            mUser = user;
            mEmail = email;
        }

        protected Integer doInBackground(String... params) {
            int response = 12345;
            try {
                response = new RegisterController().verifyUserOrEmailExists(mUser, mEmail);
            } catch (Exception e){
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onCancelled() {
            mVerifyUserTask = null;
            //showProgress(false);
        }

        protected void onPostExecute(Integer response) {
            switch (response){
                case 0:
                    saveData();
                    break;
                case 1:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userRegister.setError(getString(R.string.alreadyExistsUsername));
                            View focusView = userRegister;
                            focusView.requestFocus();
                        }
                    });
                    break;
                case 2:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emailRegister.setError(getString(R.string.alreadyExistsEmail));
                            View focusView = emailRegister;
                            focusView.requestFocus();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}