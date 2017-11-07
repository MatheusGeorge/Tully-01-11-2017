package com.example.edu.menutb.view.register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edu.menutb.R;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.view.configure.TermsActivity;
import com.example.edu.menutb.view.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by jeffkenichi on 8/19/17.
 */

public class RegisterBioActivity extends AppCompatActivity {

    Button buttonConfirm;
    CheckBox checkBoxTerms;
    private boolean enableButton = false;
    Bundle extras;
    TextView editTextCity;
    TextView editTextCountry;
    UserRegisterTask mUserRegister;
    private Button buttonGetCurrentLocation;
    RelativeLayout relativeHide;

    private static final int REQUEST_LOCATION = 1;
    private Location currentLocation = null;
    private LocationManager locationManager;
    
    private String mlatitude;
    private String mlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_place);

        extras = getIntent().getExtras();

        editTextCity = (TextView) findViewById(R.id.textViewCityRegisterBio);
        relativeHide = (RelativeLayout) findViewById(R.id.relativeHide);
        relativeHide.setVisibility(View.INVISIBLE);
        editTextCountry = (TextView) findViewById(R.id.textViewCountryRegisterBio);

        checkBoxTerms = (CheckBox) findViewById(R.id.checkBoxTermsRegisterBio);
        checkBoxTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableButton = !enableButton;
                loadTerms();
            }
        });

        buttonConfirm = (Button) findViewById(R.id.buttonConfirmRegisterBio);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptConfirm();
            }
        });

        buttonGetCurrentLocation = (Button) findViewById(R.id.getCurrentLocationButton);
        buttonGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyLocationPermission();
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void attemptConfirm() {
        String mCity = editTextCity.getText().toString();
        String mCountry = editTextCountry.getText().toString();
        View focusView = null;
        boolean cancel = false;
        //Valida se a cidade é valido
        if (TextUtils.isEmpty(mCity)) {
            editTextCity.setError(getString(R.string.errorFieldRequired));
            focusView = editTextCity;
            cancel = true;
        } else if (mCity.length() <= 4) {
            editTextCity.setError(getString(R.string.errorNameInvalid));
            focusView = editTextCity;
            cancel = true;
        }
        //Valida se o estado é valido
        if (TextUtils.isEmpty(mCountry)) {
            editTextCountry.setError(getString(R.string.errorFieldRequired));
            focusView = editTextCountry;
            cancel = true;
        } else if (mCountry.length() <= 3) {
            editTextCountry.setError(getString(R.string.errorNameInvalid));
            focusView = editTextCountry;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        if (enableButton && !cancel) {
            mUserRegister = new UserRegisterTask(extras, editTextCity.getText().toString()
                    , editTextCountry.getText().toString());
            mUserRegister.execute();
        } else if (!enableButton) {
            Snackbar.make(findViewById(R.id.linearLayoutRegisterBio), getString(R.string.termsNotAccepted), Snackbar.LENGTH_LONG)
                    .setAction(getText(R.string.close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
        }
    }

    private void loadTerms() {
        if (enableButton) {
            Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
            intent.putExtra("aparecer", "true");
            startActivity(intent);
        }
    }

    public void verifyLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Precisamos da sua localização", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            List<String> providers = locationManager.getProviders(true);
            currentLocation = locationManager.getLastKnownLocation(providers.get(0));

            mlatitude  = String.valueOf(currentLocation.getLatitude());
            mlongitude = String.valueOf(currentLocation.getLongitude());
            UserCityCountry mCityCountry = new UserCityCountry(mlatitude, mlongitude);
            mCityCountry.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (requestCode == REQUEST_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    verifyLocationPermission();
                }
                break;
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            //currentLocation = locationManager.getLastKnownLocation(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private class UserCityCountry extends AsyncTask<Object, Object, String[]> {
        private final String latitude;
        private final String longitude;
        UserCityCountry(String mlatitude, String mlongitude) {
             latitude = mlatitude;
             longitude = mlongitude;
        }

        protected String[] doInBackground(Object... params) {
            URL url = createURLGoogle();
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                int httpResult = connection.getResponseCode();
                Log.d(null, "doInBackground: " + httpResult);
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return convertJSONToArrayList(new JSONObject(builder.toString()));
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        private String[] convertJSONToArrayList(JSONObject jsonObject) {
            try {
                JSONArray results = jsonObject.getJSONArray("results");
                JSONObject address_components = results.getJSONObject(0);
                JSONArray inside_address_components = address_components.getJSONArray("address_components");
                JSONObject getKeysCity = inside_address_components.getJSONObject(5);
                String cityString = getKeysCity.getString("long_name");
                JSONObject getKeysCountry = inside_address_components.getJSONObject(inside_address_components.length()-2);
                String countryString = getKeysCountry.getString("long_name");
                String[] cc = {cityString, countryString};
                return cc;
            } catch (Exception e) {
                Log.d("Erro no Google Encoding", e.getMessage());
            }
            return null;
        }


        protected void onPostExecute(String[] cc) {
            if(cc != null){
                relativeHide.setVisibility(View.VISIBLE);
                editTextCountry.setText(String.valueOf(cc[1]));
                editTextCity.setText(String.valueOf(cc[0]));
            }
        }


        public URL createURLGoogle() {
            try {
                String urlString = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+ latitude + ",%20" + longitude;
                return new URL(urlString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class UserRegisterTask extends AsyncTask<String, Void, UserTully> {

        private final String mUsuario;
        private final String mName;
        private final String mEmail;
        private final String mPassword;
        private final String mCidade;
        private final String mPais;
        private final String mFotoUrl;

        UserRegisterTask(Bundle bundle, String cidade, String pais) {
            mName = bundle.getString("nameRegister");
            mUsuario = bundle.getString("userRegister");
            mPassword = bundle.getString("passwordRegister");
            mEmail = bundle.getString("emailRegister");
            mCidade = cidade;
            mPais = pais;
            mFotoUrl = bundle.getString("photoPerfil");
        }

        protected UserTully doInBackground(String... params) {
        URL url = createURL();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            JSONObject body = new JSONObject();
            body.put("nome", mName);
            body.put("userName", mUsuario);
            body.put("senha", mPassword);
            body.put("email", mEmail);
            body.put("cidade", mCidade);
            body.put("estado", "SP");
            body.put("pais", mPais);
            body.put("fotoPerfil", mFotoUrl);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(body.toString());
            out.close();

            int httpResult = connection.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_CREATED) {
                Snackbar.make(findViewById(R.id.linearLayoutRegisterBio), getString(R.string.userCreatedRegisterBio), Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
            }
            Log.d(null, "doInBackground: " + httpResult);
            return new UserTully();
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.linearLayoutRegisterBio), getString(R.string.connectionError), Snackbar.LENGTH_LONG)
                        .setAction("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
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
            mUserRegister = null;
            //showProgress(false);
        }

        protected void onPostExecute(UserTully userTully) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


        public URL createURL() {
            try {
                String urlString = "https://tully-api.herokuapp.com/api/usuarios";
                return new URL(urlString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

