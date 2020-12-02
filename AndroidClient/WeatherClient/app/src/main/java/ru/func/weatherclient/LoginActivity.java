package ru.func.weatherclient;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText login, password;
    private TextView message;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        message = findViewById(R.id.message);
    }

    public void login(View view) {
        if (!TextUtils.isEmpty(login.getText().toString())) {
            request = new Request.Builder()
                    .addHeader("Accept", "application/json")
                    .url("http://func-weather.herokuapp.com/mobile/check?login=" +
                            login.getText().toString() +
                            "&password=" +
                            password.getText().toString()
                    ).build();
            throwRequest();
        }
    }

    public void skip(View view) {
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    private void throwRequest() {
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                message.setText("Ошибка авторизации. Проверте введенные данные.");
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    if (response.body().string().contains("false"))
                        message.setText("Ошибка авторизации. Проверте введенные данные.");
                    else {
                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        intent.putExtra("login", login.getText().toString());
                        intent.putExtra("password", password.getText().toString());
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    message.setText("Сервер в спящем режиме. Повторите попытку через минуту.");
                }
            }
        });
    }
}
