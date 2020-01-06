package ru.func.weathersender.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.func.weathersender.entity.Sensor;
import ru.func.weathersender.util.Location;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author func 06.01.2020
 */
@RestController
@RequestMapping(path = "/modile")
public class MobileController extends DatableController {
    private static final String APPLICATION_JSON_VALUE_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8";
    private static final String APPLICATION_XML_VALUE_UTF8 = MediaType.APPLICATION_XML_VALUE + ";charset=utf-8";

    @RequestMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE_UTF8)
    public List<Sensor> sendMobileNewDataJson() {
        return getSensorList();
    }

    @RequestMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_XML_VALUE,
            produces = APPLICATION_XML_VALUE_UTF8)
    public List<Sensor> sendMobileNewDataXml() {
        return getSensorList();
    }

    private List<Sensor> getSensorList() {
        return Stream.of(Location.values())
                .map(location -> sensorRepository.findNewestSensorByLocation(location.getLocation()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}

