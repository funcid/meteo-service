package ru.func.weatherclient;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.FragmentActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng chosenMarker;
    private String data = "Загрузка...", nearData = "";
    private List<Marker> markerList = new ArrayList<>();
    private TextView output;
    private JSONArray json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        output = findViewById(R.id.output);
        output.setText(data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng startLocation = new LatLng(55, 37);
        markerList.add(mMap.addMarker(
                new MarkerOptions()
                        .position(startLocation)
                        .title(data)
        ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        chosenMarker = marker.getPosition();
                        return false;
                    }
                }
        );

        new Timer().schedule(new TimerTask() {
            int secondsTemp = 0, delayUpdate = -4;
            String chosenData = "";
            boolean first = true;

            @Override
            public void run() {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (secondsTemp == delayUpdate || first) {

                            secondsTemp = 0;
                            delayUpdate = delayUpdate > 30 ? delayUpdate : delayUpdate + 5;
                            Request request = new Request.Builder()
                                    .addHeader("Accept", "application/json")
                                    .url("http://func-weather.herokuapp.com/mobile")
                                    .build();
                            new OkHttpClient().newCall(request)
                                    .enqueue(new Callback() {
                                        @Override
                                        public void onFailure(final Call call, IOException e) { }
                                        @Override
                                        public void onResponse(Call call, final Response response) throws IOException {
                                            try {
                                                json = new JSONArray(response.body().string());
                                                System.out.println(json.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            for (Marker marker : markerList)
                                marker.remove();
                            try {
                                if (json != null) {
                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject sensor = json.getJSONObject(i);

                                        String[] cords = sensor.getString("location").split(", ");
                                        LatLng location = new LatLng(
                                                Float.parseFloat(cords[0]),
                                                Float.parseFloat(cords[1])
                                        );
                                        nearData = sensor.getString("temperature") + " C°" +
                                                sensor.getString("pressure") + " torr" +
                                                sensor.getString("humidity") + "%";
                                        if (location.equals(chosenMarker))
                                            chosenData = nearData;
                                        markerList.add(
                                                mMap.addMarker(new MarkerOptions()
                                                        .position(location)
                                                        .title(nearData)
                                                )
                                        );
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            first = false;
                        }
                        secondsTemp++;
                        output.setTextSize(21);
                        output.setText("" +
                                "Обновление через " +
                                (delayUpdate - secondsTemp) +
                                " сек.\n" +
                                (chosenData.isEmpty() ? nearData : chosenData)
                        );
                    }
                });
            }
        }, 0, 1000);
    }
}