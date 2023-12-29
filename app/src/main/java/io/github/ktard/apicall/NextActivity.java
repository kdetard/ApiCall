package io.github.ktard.apicall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.moshi.Moshi;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev")
                .addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder().build()))
                .client(new OkHttpClient.Builder().build()) // Use OkHttp client if needed
                .build();

        OpenRemoteService apiService = retrofit.create(OpenRemoteService.class);

        findViewById(R.id.filledButton).setOnClickListener(v -> {
            apiService.getUserRoles()
                    .enqueue(new Callback<List<UserRole>>() {
                        @Override
                        public void onResponse(Call<List<UserRole>> call, Response<List<UserRole>> response) {
                            List<UserRole> roles = response.body();
                            if (roles != null && !roles.isEmpty()) {
                                ((TextInputLayout)findViewById(R.id.name)).getEditText().setText(roles.stream().findFirst().orElse(null).name);
                                ((TextInputLayout)findViewById(R.id.desc)).getEditText().setText(roles.stream().findFirst().orElse(null).description);
                                return;
                            }
                            Toast.makeText(NextActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<List<UserRole>> call, Throwable t) {
                            Log.d("Main", Objects.requireNonNull(t.getMessage()));
                            Toast.makeText(NextActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}