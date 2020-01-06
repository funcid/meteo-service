package ru.func.weathersender.parser;

import ru.func.weathersender.entity.Sensor;

import java.util.List;

/**
 * @author func 06.01.2020
 */
public interface SensorDataParser {

    String parseSensorToFormat(List<Sensor> sensor);
}
