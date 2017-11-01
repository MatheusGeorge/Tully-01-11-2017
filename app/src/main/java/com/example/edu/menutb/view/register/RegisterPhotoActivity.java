package com.example.edu.menutb.view.register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edu.menutb.R;
import com.example.edu.menutb.model.asynchronous.MultpartUtility;
import com.example.edu.menutb.model.photo.BitmapResizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Classe para pegar a foto de perfil dá pessoa na hora do cadastro
 * Created by jeffkenichi on 8/18/17.
 */

public class RegisterPhotoActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2000;
    Bundle extras;
    Button buttonSkipRegisterPhoto;
    Button buttonNextRegisterPhoto;
    ImageView imageViewPhotoProfile;
    ImageButton imageButtonCameraRegister;
    ImageButton imageButtonGalleryRegister;
    Bitmap photoPerfil;

    PostImageTask postImageTask;
    String imagePath;

    Boolean camera = false;

    Boolean tirouFoto = false;

    private Boolean requestCamera = false;
    private Boolean requestGallery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_photo);

        //Para pegar as intent.putExtras passada pela Acitivity Register
        extras = getIntent().getExtras();

        imageViewPhotoProfile = (ImageView) findViewById(R.id.imageViewPhotoPerfil);

        imageButtonCameraRegister = (ImageButton) findViewById(R.id.imageButtonCameraRegister);
        imageButtonCameraRegister.setOnClickListener(listenerPhoto);

        imageButtonGalleryRegister = (ImageButton) findViewById(R.id.imageButtonGalleryRegister);
        imageButtonGalleryRegister.setOnClickListener(listenerGallery);

        buttonSkipRegisterPhoto = (Button) findViewById(R.id.buttonSkipRegisterPhoto);
        buttonSkipRegisterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToBio(false, null);
            }
        });

        buttonNextRegisterPhoto = (Button) findViewById(R.id.buttonNextRegisterPhoto);
        buttonNextRegisterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tirouFoto)
                    sendDataToBio(true, camera);
                else
                    Toast.makeText(getApplicationContext(), "É preciso tirar uma foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableNextButton(Boolean enable) {
        if (enable)
            buttonNextRegisterPhoto.setEnabled(true);
        else
            buttonNextRegisterPhoto.setEnabled(false);
    }

    private void sendDataToBio(Boolean havePhoto, Boolean camera) {
        Intent intent = new Intent(getApplicationContext(), RegisterBioActivity.class);
        if (havePhoto && camera != null) {
            postImageTask = new PostImageTask(photoPerfil, imagePath);
            postImageTask.execute(camera ? "camera" : "gallery");
        } else {
            intent.putExtra("nameRegister", extras.getString("nameRegister"));
            intent.putExtra("emailRegister", extras.getString("emailRegister"));
            intent.putExtra("userRegister", extras.getString("userRegister"));
            intent.putExtra("passwordRegister", extras.getString("passwordRegister"));
            intent.putExtra("confirmPasswordRegister", extras.getString("confirmPasswordRegister"));
            startActivity(intent);
        }
    }

    private final View.OnClickListener listenerGallery = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            requestGallery = true;
            checkGalleryPermission();
        }
    };

    private void checkGalleryPermission() {
        pickImage();
    }

    private final View.OnClickListener listenerPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View V) {
            requestCamera = true;
            checkCameraPermission();
        }
    };

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Precisamos da sua câmera ", Toast.LENGTH_SHORT).show();
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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao criar o arquivo", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.edu.menutb", photoFile);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(requestCamera = false && requestGallery){
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    }
                }
                break;
            case REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent();
                        Log.d(null, "onRequestPermissionsResult: CAIU NO REQUEST PERMISSIONS");
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                imagePath = getPath(imageUri);
                imageStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageViewPhotoProfile.setImageBitmap(selectedImage);
                imageViewPhotoProfile.setScaleType(ImageView.ScaleType.CENTER_CROP); //nao pode usar scaleType.FIT
                camera = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                photoPerfil = (Bitmap) extras.get("data");
                Log.d(null, "onActivityResult: ENTROU NO REQUEST CAMERA");
                imageViewPhotoProfile.setImageBitmap(photoPerfil);
                imageViewPhotoProfile.setScaleType(ImageView.ScaleType.CENTER_CROP); //nao pode usar scaleType.FIT
                tirouFoto = true;
                camera = true;
            } else {
                Log.d(null, "onActivityResult: DATA NULOO");
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void pickImage() {
        if (ActivityCompat.checkSelfPermission(RegisterPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterPhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
        }
    }

    private class PostImageTask extends AsyncTask<String, Void, String> {
        private Bitmap mPhotoPerfil;
        private String imagePath;
        private MultpartUtility multpartUtility;

        public PostImageTask(Bitmap imageView, String imagePath) {
            this.mPhotoPerfil = imageView;
            this.imagePath = imagePath;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            switch (params[0]) {
                case "gallery":
                    int day, month, year;
                    int second, minute, hour;
                    GregorianCalendar date = new GregorianCalendar();

                    day = date.get(Calendar.DAY_OF_MONTH);
                    month = date.get(Calendar.MONTH);
                    year = date.get(Calendar.YEAR);

                    second = date.get(Calendar.SECOND);
                    minute = date.get(Calendar.MINUTE);
                    hour = date.get(Calendar.HOUR);

                    String name = (hour + "" + minute + "" + second + "" + day + "" + (month + 1) + "" + year);
                    String tag = name + ".jpg";
                    String fileName = imagePath.replace(imagePath, tag);
                    HttpURLConnection conn = null;
                    DataOutputStream dos = null;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1 * 1024 * 1024;

                    File sourceFile = new File(imagePath);

                    try {
                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        URL url = new URL("https://tully-api.herokuapp.com/api/files/foto_perfil");

                        // Open a HTTP  connection to  the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("image", fileName);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
                                + fileName + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of  maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        }

                        // send multipart form data necesssary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();

                        Log.d(null, "HTTP Response is : "
                                + serverResponseMessage + ": " + serverResponseCode);

                        if (serverResponseCode == 200) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(RegisterPhotoActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            StringBuilder builder = new StringBuilder();
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    builder.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            response = convertJSONToUrl(new JSONObject(builder.toString()));
                        }

                        //close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "camera":
                    OutputStream outputStream = null;
                    try {

                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        outputStream = new FileOutputStream(imagePath);

                        bitmap = new BitmapResizer().bitmapResizer(bitmap, 512, 512);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        File f = new File(imagePath);

                        String url = "https://tully-api.herokuapp.com/api/files/foto_perfil";

                        multpartUtility = new MultpartUtility(url, f.getName());
                        multpartUtility.addFilePart("image", f);
                        JSONObject object = multpartUtility.finish();

                        response = object.getString("fileUrl");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return response;
        }

        protected void onPostExecute(String photoUrl) {
            Intent intent = new Intent(getApplicationContext(), RegisterBioActivity.class);
            intent.putExtra("photoPerfil", photoUrl);
            intent.putExtra("nameRegister", extras.getString("nameRegister"));
            intent.putExtra("emailRegister", extras.getString("emailRegister"));
            intent.putExtra("userRegister", extras.getString("userRegister"));
            intent.putExtra("passwordRegister", extras.getString("passwordRegister"));
            intent.putExtra("confirmPasswordRegister", extras.getString("confirmPasswordRegister"));
            startActivity(intent);
        }
    }

    private String convertJSONToUrl(JSONObject jsonObject) {
        String fotoUrl = "";
        try {
            fotoUrl = jsonObject.getString("fileUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fotoUrl;
    }
}