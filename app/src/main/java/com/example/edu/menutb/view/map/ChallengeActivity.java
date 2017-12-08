package com.example.edu.menutb.view.map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ChallengeController;
import com.example.edu.menutb.dao.ProfileDAO;
import com.example.edu.menutb.model.challenge.Challenge;
import com.example.edu.menutb.model.map.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nostra13.universalimageloader.core.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by Edu on 18/07/2017.
 * Classe que carrega o mapa e mostra os desafios
 */

public class ChallengeActivity extends Fragment implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_CAMERA = 12345;
    private static final int PICK_FROM_GALLERY = 2;
    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = 180.0f;


    ProgressDialog dialog;


    GoogleMap mGoogleMap;
    MapView mMapView;
    private Marker marker;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    View myView;
    private Location currentLocation = null;
    private LatLng loc = null;

    //Bottom Sheet
    private LinearLayout llBottomSheet;
    private BottomSheetBehavior llBottomSheetBehavior;
    private FloatingActionButton fabDetails;

    //Bottom Sheet
    TextView textViewChallengeAdress;
    TextView textViewChallengeCountry;
    TextView textViewChallengeState;
    TextView textViewChallengeCity;
    TextView textViewChallengePhone;
    TextView textViewChallengeSite;
    TextView textViewChallengeDescription;
    TextView textViewName;
    TextView textViewWon;
    ImageView imageViewChallengeBackground;
    ImageView imageViewChallengeExpand;
    TextView textViewDistance;
    LinearLayout linearLayoutDoChallenge;
    GetChallengeTask mGetChallengeTask;
    LocationManager locationManager;
    Button buttonDoChallenge;
    FloatingActionButton fabCancel;
    Boolean doChallenge = false;
    String tokenString;
    String idString;
    public static Marker markerClickado;
    PostImageTask mPostImageTask;
    String imagePath;
    String idDesafio;
    String realizado = "false";

    Boolean expanded = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = (ViewGroup) inflater.inflate(R.layout.challenge_map, container, false);
        super.onCreate(savedInstanceState);
        mMapView = (MapView) myView.findViewById(R.id.map);
        getActivity().setTitle("");
        doChallenge = false;

        textViewChallengeAdress = (TextView) myView.findViewById(R.id.textViewChallengeAdress);
        textViewChallengeCountry = (TextView) myView.findViewById(R.id.textViewChallengeCountry);
        textViewChallengeState = (TextView) myView.findViewById(R.id.textViewChallengeState);
        textViewChallengeCity = (TextView) myView.findViewById(R.id.textViewChallengeCity);
        textViewChallengePhone = (TextView) myView.findViewById(R.id.textViewChallengePhone);
        textViewChallengeSite = (TextView) myView.findViewById(R.id.textViewChallengeSite);
        textViewChallengeDescription = (TextView) myView.findViewById(R.id.textViewChallengeDescription);
        textViewName = (TextView) myView.findViewById(R.id.textViewChallengeName);
        textViewWon = (TextView) myView.findViewById(R.id.textViewChallengeXP);
        imageViewChallengeBackground = (ImageView) myView.findViewById(R.id.imageViewChallengeBackground);
        buttonDoChallenge = (Button) myView.findViewById(R.id.buttonDoChallenge);
        imageViewChallengeExpand = (ImageView) myView.findViewById(R.id.imageViewChallengeExpand);
        linearLayoutDoChallenge = (LinearLayout) myView.findViewById(R.id.linearLayoutDoChallenge);
        textViewDistance = (TextView) myView.findViewById(R.id.textViewDistanceValue);

        SharedPreferences id = getContext().getSharedPreferences("id", Context.MODE_PRIVATE);
        idString = id.getString("id", "");
        SharedPreferences token = getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");

        fabCancel = (FloatingActionButton) myView.findViewById(R.id.fabCancel);
        fabCancel.setVisibility(View.INVISIBLE);
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new ChallengeActivity()).commit();
            }
        });

        imageViewChallengeExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expanded)
                    llBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                else
                    llBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        buttonDoChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleMap.clear();
                Double currentLocationLat = currentLocation.getLatitude();
                Double currentLocationLong = currentLocation.getLongitude();
                LatLng origin = new LatLng(currentLocationLat, currentLocationLong);
                LatLng dest = markerClickado.getPosition();
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(markerClickado.getPosition())
                        .title(markerClickado.getTitle())
                        .snippet(markerClickado.getSnippet()).icon(BitmapDescriptorFactory.defaultMarker(realizado.equals("true") ?
                                BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED)));


                doChallenge = true;
                fabCancel.setVisibility(View.VISIBLE);

                loadPath(origin, dest);
                fabDetails.setVisibility(View.VISIBLE);
                llBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                linearLayoutDoChallenge.setVisibility(View.INVISIBLE);
            }
        });


        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_logo)        //    Display Stub Image
                .showImageForEmptyUri(R.mipmap.ic_logo)    //    If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        initBottomSheet();
        initFab();

        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        return myView;
    }

    private void loadPath(LatLng origin, LatLng dest) {

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Inicia a thread para fazer a consulta na api
        mGetChallengeTask = new GetChallengeTask();
        mGetChallengeTask.execute();

        //Verifica a permissão da localização
        verifyLocationPermission();
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                writeInFields(marker.getTitle(), marker.getSnippet(), marker.getPosition());
                markerClickado = marker;

                if (doChallenge) {
                    fabDetails.setVisibility(View.VISIBLE);
                }
                llBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fabDetails.setVisibility(View.INVISIBLE);
                llBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            if (doChallenge) {
                Double currentLocationLat = currentLocation.getLatitude();
                Double currentLocationLong = currentLocation.getLongitude();
                LatLng origin = new LatLng(currentLocationLat, currentLocationLong);
                LatLng dest = markerClickado.getPosition();
                loadPath(origin, dest);
            }
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

    private void rotateIcon(){
        if(expanded)
            imageViewChallengeExpand.animate().rotation(180).start();
        else
            imageViewChallengeExpand.animate().rotation(0).start();
    }

    public void checkin() {
        if (mGoogleMap == null) { //somente se o mapa estiver pronto
            Toast.makeText(getContext(), "Mapa não está pronto", Toast.LENGTH_LONG).show();
        } else if (currentLocation == null) {
            Toast.makeText(getContext(), "Sem sinal GPS", Toast.LENGTH_LONG).show();
            loc = new LatLng(-23.5631338, -46.6543286);//vai pra Paulista
            moverLocalFinal(loc);

        } else {
            loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            moverLocalFinal(loc);
        }
    }

    private void moverLocalFinal(LatLng loc) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(17).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void verifyLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getContext(), "Precisamos da sua localização", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            List<String> providers = locationManager.getProviders(true);
            //Talvez tá dando pau aq quando ele acaba de dar a permissão pela primeira vez
            currentLocation = locationManager.getLastKnownLocation(providers.get(0));
            mGoogleMap.setMyLocationEnabled(true);
            checkin();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent();
                        Log.d(null, "onRequestPermissionsResult: CAIU NO REQUEST PERMISSIONS");
                    }
                }
                break;
            case REQUEST_LOCATION:
                if (requestCode == REQUEST_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    verifyLocationPermission();
                } else {
                    // We were not granted permission this time, so don't try to show the contact picker
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
        }
    }

    private void writeInFields(String title, String sb, LatLng latLng) {
        String data[] = sb.split(";");
        Log.d(null, "stringbuilder: " + sb.toString());
        realizado = data[9];
        idDesafio = data[8];
        Location locationDest = new Location("");
        if(currentLocation!=null){
            locationDest.setLongitude(latLng.longitude);
            locationDest.setLatitude(latLng.latitude);
            String distanceInmeters = new ChallengeController().convertDistanceToString(currentLocation.distanceTo(locationDest));
            textViewDistance.setText(distanceInmeters);
            if (new ChallengeController().outOfRange(currentLocation.distanceTo(locationDest)))
                linearLayoutDoChallenge.setVisibility(View.INVISIBLE);
            else
                linearLayoutDoChallenge.setVisibility(View.VISIBLE);
        } else {
            textViewDistance.setText("0");
            linearLayoutDoChallenge.setVisibility(View.INVISIBLE);
        }
        textViewWon.setText((realizado.equals("true"))?(getContext().getText(R.string.challengeExp) + " 0xp"):(getContext().getText(R.string.challengeExp)+ " 10xp"));
        textViewName.setText(title);
        textViewChallengeAdress.setText(data[0]);
        textViewChallengeCountry.setText(data[1]);
        textViewChallengeState.setText(data[2]);
        textViewChallengeCity.setText(data[3]);
        textViewChallengePhone.setText(data[4]);
        textViewChallengeSite.setText(data[5]);
        Linkify.addLinks(textViewChallengeSite, Linkify.WEB_URLS);
        textViewChallengeDescription.setText(data[6]);
        new LoadImageTask(imageViewChallengeBackground).execute(data[7]);
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                bitmap = new ChallengeController().loadPhotoChallenge(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    private void initBottomSheet() {
        llBottomSheet = (LinearLayout) myView.findViewById(R.id.bottom_sheet);
        llBottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        llBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        llBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    if (doChallenge) {
                        fabDetails.animate().scaleX(1).scaleY(1).setDuration(300).start();
                        fabCancel.setVisibility(View.VISIBLE);
                    }
                    expanded = false;
                    rotateIcon();
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    fabDetails.animate().scaleX(0).scaleY(0).setDuration(300).start();
                    fabCancel.setVisibility(View.INVISIBLE);
                    expanded = true;
                    rotateIcon();
                } else if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    fabDetails.setVisibility(View.INVISIBLE);
                    expanded = false;
                    rotateIcon();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    private void initFab() {
        fabDetails = (FloatingActionButton) myView.findViewById(R.id.fab_camera);
        fabDetails.setVisibility(View.INVISIBLE);
        fabDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doChallenge) {
                    LatLng latLngDest = markerClickado.getPosition();
                    Location locationDest = new Location("");
                    locationDest.setLatitude(latLngDest.latitude);
                    locationDest.setLongitude(latLngDest.longitude);
                    double distanceInmeters = currentLocation.distanceTo(locationDest);
                    Log.d(null, "DISTANCIA: " + distanceInmeters);
                    if (distanceInmeters <= 4000) { //deixar 30 dps
                        checkCameraPermission();
                    } else {
                        Toast.makeText(getActivity(), "Você ainda não está no local para tirar a foto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class GetChallengeTask extends AsyncTask<String, Void, ArrayList<Challenge>> {
        protected ArrayList<Challenge> doInBackground(String... params) {
            ArrayList<Challenge> arrayListChallenges = new ArrayList<>();
            try {
                arrayListChallenges = new ChallengeController().loadAllChallenges(tokenString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayListChallenges;
        }

        @Override
        protected void onCancelled() {
            mGetChallengeTask = null;
        }

        protected void onPostExecute(ArrayList<Challenge> arrayListChallenges) {

            for (Challenge challenge : arrayListChallenges) {
                //Porco dmais
                String sb = challenge.getEndereco() + ";" + challenge.getPais() + ";" + challenge.getEstado()
                        + ";" + challenge.getCidade() + ";" + challenge.getTelefone() + ";" + challenge.getSite()
                        + ";" + challenge.getDescricao() + ";" + challenge.getFoto() + ";" + challenge.getId() + ";" + challenge.getRealizado();

                LatLng latLng = new LatLng(Double.parseDouble(challenge.getLatitude()), Double.parseDouble(challenge.getLongitude()));
                //CRIA O MARKER NO MAPA COM A POSIÇÃO TITULO E SUBTITULO
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(challenge.getNome())
                        .snippet(sb)
                        .icon(BitmapDescriptorFactory.defaultMarker(challenge.getRealizado().equals("true") ?
                                BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(getContext(), "Precisamos da sua câmera ", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);

        } /*else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);

        } */else {

            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Erro ao criar o arquivo", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.example.edu.menutb", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd:HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        imagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Log.d(null, "onActivityResult: ENTROU NO REQUEST CAMERA");
            mPostImageTask = new PostImageTask(imagePath);
            mPostImageTask.execute();
            //riv.setScaleType(ImageView.ScaleType.CENTER_CROP); //nao pode usar scaleType.FIT
        } else {
            Log.d(null, "onActivityResult: DATA NULOO");
        }
    }

    private class PostImageTask extends AsyncTask<String, Void, String> {
        private String imagePath;

        public PostImageTask(String imagePath) {
            this.imagePath = imagePath;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = ProgressDialog.show(getContext(), "", getText(R.string.loginAccept), true);
                    }
                });
                response = new ChallengeController().postPhotoToFirebase(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onCancelled() {
            mPostImageTask = null;
        }

        protected void onPostExecute(String response) {
            PathImageTask mPathImage = new PathImageTask(response);
            mPathImage.execute();
            updateExperience(realizado);
            dialog.dismiss();
            showMessage();
        }

    }

    private void updateExperience(final String realizado) {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if (!realizado.equals("true"))
                    return new ProfileDAO(idString, getContext()).updateExperience(10);
                return null;
            }
        };
        task.execute();
    }

    private void showMessage() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        final View alterarView = layoutInflaterAndroid.inflate(R.layout.challenge_dialog_complete, null);
        AlertDialog.Builder dialogName = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        dialogName.setView(alterarView);
        dialogName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogBox, int id) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new ChallengeActivity()).commit();
            }
        });
        AlertDialog dialogFinalName = dialogName.create();
        dialogFinalName.show();
        dialogFinalName.getButton(dialogFinalName.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBranco));
    }


    private class PathImageTask extends AsyncTask<String, Void, String> {
        private String urlFoto;

        public PathImageTask(String url) {
            this.urlFoto = url;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                new ChallengeController().postPhotoChallenge(idString, tokenString, urlFoto, idDesafio);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
