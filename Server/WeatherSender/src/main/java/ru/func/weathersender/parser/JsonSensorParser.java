package ru.func.weathersender.parser;

import com.google.gson.Gson;
import ru.func.weathersender.entity.Sensor;

import java.util.List;

/**
 * @author func 06.01.2020
 */
public class JsonSensorParser implements SensorDataParser {

    private Gson gson = new Gson();

    @Override
    public String parseSensorToFormat(List<Sensor> sensor) {
        return gson.toJson(sensor);
    }
}
