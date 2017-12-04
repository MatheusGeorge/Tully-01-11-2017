package com.example.edu.menutb.view.configure;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.edu.menutb.R;
import com.example.edu.menutb.model.UserTully;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pc gravacao on 26/08/2017.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    boolean cancel = false;
    Bundle extras;
    EditText oldPassword;
    EditText newPassword;
    EditText confirmNewPassword;
    Button save;
    private View changeLinearLayout;
    private UserLoginTask mAuthTask = null;
    LinearLayout base;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_change_password);
        oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
        newPassword = (EditText) findViewById(R.id.newPasswordEditTextt);
        changeLinearLayout = findViewById(R.id.changeLinearLayout);
        extras = getIntent().getExtras();
        confirmNewPassword = (EditText) findViewById(R.id.confirmPasswordEditTextt);
        save = (Button) findViewById(R.id.saveButton);
        confirmNewPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(base.getWindowToken(), 0);
                verifyFields();
            }
        });
        base = (LinearLayout) findViewById(R.id.changeLinearLayout);
        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(base.getWindowToken(), 0);

            }
        });
    }

    private boolean isPasswordValid(String password){
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    private void verifyFields() {
        String sOldPassword = oldPassword.getText().toString();
        String sNewPassword = newPassword.getText().toString();
        String sConfirmNewPassword = confirmNewPassword.getText().toString();
        cancel = false;
        View focusView = null;

        //Check for a valid password
        if(TextUtils.isEmpty(sNewPassword)){
            newPassword.setError(getString(R.string.errorFieldRequired));
            focusView = newPassword;
            cancel = true;
        } else if(!isPasswordValid(sNewPassword)){
            newPassword.setError(getText(R.string.changePasswordInvalid));
            focusView = newPassword;
            cancel = true;
        }
        //Check if the confirmPassword is equals
        if(TextUtils.isEmpty(sConfirmNewPassword)){
            confirmNewPassword.setError(getString(R.string.errorFieldRequired));
            focusView = confirmNewPassword;
            cancel = true;
        } else if(!isConfirmPasswordValid(sNewPassword, sConfirmNewPassword)) {
            confirmNewPassword.setError(getString(R.string.errorConfirmPasswordInvalid));
            focusView = confirmNewPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(sOldPassword, sNewPassword);
            mAuthTask.execute();
        }
    }

    private class UserLoginTask extends AsyncTask<String, Void, UserTully> {

        private final String msOldPassword;
        private final String msNewPassword;

        UserLoginTask(String email, String password) {
            msOldPassword = email;
            msNewPassword = password;
        }

        protected UserTully doInBackground(String... params) {
            URL url = createURL();
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Authorization", "bearer " + extras.getString("token"));
                connection.connect();

                JSONObject body = new JSONObject();
                body.put("senhaAtual", msOldPassword);
                body.put("senhaNova", msNewPassword);

                OutputStreamWriter out = new   OutputStreamWriter(connection.getOutputStream());
                out.write(body.toString());
                out.close();

                int httpResult = connection.getResponseCode();

                Log.d(null, "doInBackground: "+httpResult);

                if(httpResult == HttpURLConnection.HTTP_OK){
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return convertJSONToUser(new JSONObject(builder.toString()));
                }else if(httpResult == HttpURLConnection.HTTP_BAD_REQUEST){
                    Snackbar.make(changeLinearLayout, getString(R.string.passwordWrongChange), Snackbar.LENGTH_SHORT)
                            .setAction("", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
                }else{
                    System.out.println(connection.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }

        protected void onPostExecute(UserTully userTully) {
            if(!cancel){
                Snackbar.make(changeLinearLayout, getString(R.string.changeSucessfull), Snackbar.LENGTH_SHORT)
                        .setAction("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
            }
        }
    }

    public URL createURL() {
        String id = extras.getString("id");
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/" + id + "/senha";
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserTully convertJSONToUser(JSONObject userJSON){

        return new UserTully(extras.getString("token"),"eu",extras.getString("id") );
    }
}
