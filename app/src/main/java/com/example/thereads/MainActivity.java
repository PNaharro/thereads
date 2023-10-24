package com.example.thereads;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
     private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button loadImageButton = findViewById(R.id.loadImageButton);
        handler = new Handler(Looper.getMainLooper());

        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRandomImage();
            }
        });
    }

    private void loadRandomImage() {
        new Thread(() -> {
            try {
                URL apiUrl = new URL("https://randomfox.ca/floof/");
                HttpURLConnection httpURLConnection = (HttpURLConnection) apiUrl.openConnection();

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }


                    JSONObject jsonObject = new JSONObject(response.toString());
                    String imageUrl = jsonObject.getString("image");
                    loadAndDisplayImage(imageUrl);
                } else {
                    Log.e("Error", "Error de respuesta HTTP: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    private void loadAndDisplayImage(String imageUrl) {
        new Thread(() -> {
            try {
                URL ImageUrl = new URL(imageUrl);
                InputStream in = ImageUrl.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                handler.post(() -> imageView.setImageBitmap(bitmap));
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }
}



