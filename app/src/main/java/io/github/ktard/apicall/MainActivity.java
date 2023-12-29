package io.github.ktard.apicall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.moshi.Moshi;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Replace "YOUR_BASE_URL" with your actual base URL
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev")
                .addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder().build()))
                .client(new OkHttpClient.Builder().addInterceptor(logging).build()) // Use OkHttp client if needed
                .build();

        OpenRemoteService apiService = retrofit.create(OpenRemoteService.class);

        findViewById(R.id.filledButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, NextActivity.class);
            startActivity(intent);
        });

        apiService.getUserInfo()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        if (user != null) {
                            ((TextInputLayout)findViewById(R.id.firstName)).getEditText().setText(user.firstName);
                            ((TextInputLayout)findViewById(R.id.email)).getEditText().setText(user.email);
                            ((TextInputLayout)findViewById(R.id.lastName)).getEditText().setText(user.lastName);
                            ((TextInputLayout)findViewById(R.id.username)).getEditText().setText(user.username);
                            return;
                        }
                        Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Main", Objects.requireNonNull(t.getMessage()));
                        Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}