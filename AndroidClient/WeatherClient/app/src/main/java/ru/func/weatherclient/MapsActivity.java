package ru.func.weatherclient;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import androidx.fragment.app.FragmentActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng location;
    private String data = "Обновление";
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        location = new LatLng(55.860408, 37.639443);
        marker = mMap.addMarker(new MarkerOptions().position(location).title(data));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Request request = new Request.Builder()
                        .url("http://func-weather.herokuapp.com")
                        .build();

                new OkHttpClient().newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(final Call call, IOException e) {
                                data = "Ошибка";
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                data = response.body().string();
                                data = data.split("SVAO")[data.split("SVAO").length - 1];
                                String[] strings = data
                                        .split("]")[0]
                                        .replace("[", "")
                                        .split("&");
                                data = strings[1].replace("temperature=", "") + " C*,  ";
                                data += strings[2].replace("pressure=", "") + " мм.р.с,  ";
                                data += strings[3].replace("humidity=", "") + " %";
                            }
                        });
                marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(location).title(data));
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
