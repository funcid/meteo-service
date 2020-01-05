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
    private String[] lines = {};

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
            int secondsTemp = 0, delayUpdate = 10, waitUpdater = 5;
            String chosenData = "";

            @Override
            public void run() {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (waitUpdater > -5)
                            waitUpdater--;
                        if (waitUpdater > 0)
                            return;
                        else if (secondsTemp == delayUpdate || waitUpdater > -3) {
                            secondsTemp = 1;
                            Request request = new Request.Builder()
                                    .url("http://func-weather.herokuapp.com/mobile")
                                    .build();
                            new OkHttpClient().newCall(request)
                                    .enqueue(new Callback() {
                                        @Override
                                        public void onFailure(final Call call, IOException e) {
                                        }

                                        @Override
                                        public void onResponse(Call call, final Response response) throws IOException{
                                            lines = response.body().string().split("#");
                                        }
                                    });
                            for (Marker marker : markerList)
                                marker.remove();
                            if (lines.length > 0)
                                for (String line : lines) {
                                    String[] splited = line
                                            .replace("<html>", "")
                                            .replace("<br>", "")
                                            .replace("</html>", "")
                                            .split(":");
                                    String[] cords = splited[0].split(",");
                                    if (cords.length > 1) {
                                        LatLng location = new LatLng(
                                                Float.parseFloat(cords[0]),
                                                Float.parseFloat(cords[1])
                                        );
                                        nearData = splited[1]
                                                .replace("temperature=", "")
                                                .replace(";pressure=", "C° ")
                                                .replace(";humidity=", "torr ")
                                                + "%";
                                        if (location.equals(chosenMarker))
                                            chosenData = nearData;
                                        markerList.add(
                                                mMap.addMarker(new MarkerOptions()
                                                        .position(location)
                                                        .title(nearData)
                                                ));
                                    }
                                }
                        } else
                            secondsTemp++;
                        output.setTextSize(21);
                        output.setText("" +
                                "Обновление через " +
                                (delayUpdate - secondsTemp + 1) +
                                " сек.\n" +
                                (chosenData.isEmpty() ? nearData : chosenData)
                        );
                    }
                });
            }
        }, 0, 1000);
    }
}

