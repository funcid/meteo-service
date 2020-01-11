package ru.func.weatherclient;

import android.content.pm.ActivityInfo;
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
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.FragmentActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String DATA_MESSAGE = "Обновление через %d сек.\n%s";
    private static final Request REQUEST = new Request.Builder()
            .addHeader("Accept", "application/json")
            .url("http://func-weather.herokuapp.com/mobile")
            .build();

    private GoogleMap mMap;
    private LatLng chosenMarker;
    private String nearData = "";
    private List<Marker> markerList = new ArrayList<>();
    private TextView output;
    private JSONArray json;
    private int secondsTemp = 0;
    private int delayUpdate = -4;
    private String chosenData = "";
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        output = findViewById(R.id.output);
        output.setText("Загрузка...");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        chosenMarker = marker.getPosition();
                        chosenData = markerList.get(markerList.indexOf(marker)).getTitle();
                        return false;
                    }
                }
        );

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateActivityUI();
            }
        }, 100, 1000);
    }

    private void initJsonDataFromServer() {
        new OkHttpClient().newCall(REQUEST).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                throw new RuntimeException("Cannot get data from server. " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    json = new JSONArray(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateActivityUI() {
        MapsActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if (secondsTemp == delayUpdate || first) {
                    secondsTemp = 0;
                    delayUpdate = delayUpdate > 30 ? delayUpdate : delayUpdate + 5;
                    for (Marker marker : markerList)
                        marker.remove();
                    try {
                        initJsonDataFromServer();
                        if (json != null) {
                            parseJsonArrayData();
                        }
                    } catch (JSONException | RuntimeException e) {
                        e.printStackTrace();
                    }
                    first = false;
                }
                updateOutputData();
                secondsTemp++;
            }
        });
    }

    private void parseJsonArrayData() throws JSONException {
        for (int i = 0; i < json.length(); i++) {
            JSONObject sensor = json.getJSONObject(i);

            String[] cords = sensor.getString("location").split(", ");
            LatLng location = new LatLng(
                    Float.parseFloat(cords[0]),
                    Float.parseFloat(cords[1])
            );
            nearData = String.format(
                    Locale.ENGLISH,
                    "%sC° %storr %s",
                    sensor.getString("temperature"),
                    sensor.getString("pressure"),
                    sensor.getString("humidity")
            ) + "%";
            if (location.equals(chosenMarker)) {
                chosenData = nearData;
            }
            markerList.add(
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(nearData)
                    )
            );
        }
    }

    private void updateOutputData() {
        output.setTextSize(21);
        output.setText(String.format(
                Locale.US,
                DATA_MESSAGE,
                delayUpdate - secondsTemp - 1,
                chosenData.isEmpty() ? nearData : chosenData
        ));
    }
}