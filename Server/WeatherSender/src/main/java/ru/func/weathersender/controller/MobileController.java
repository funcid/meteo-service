package ru.func.weathersender.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.func.weathersender.entity.Sensor;
import ru.func.weathersender.parser.JsonSensorParser;
import ru.func.weathersender.parser.SensorDataParser;
import ru.func.weathersender.parser.XmlSensorParser;
import ru.func.weathersender.util.Location;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author func 06.01.2020
 */
@RestController
public class MobileController extends DatableController {

    @RequestMapping(value = "/mobile", produces={"application/json","application/xml"})
    public ResponseEntity<String> sendMobileNewData(
            @RequestParam(name = "type", required = false, defaultValue = "xml") String parseType
    ) throws ParserConfigurationException {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/" + parseType + "; charset=utf-8");

        List<Sensor> sensorsToSend = new ArrayList<>();

        for (Location location : Location.values())
            sensorRepository.findNewestSensorByLocation(location.getLocation()).ifPresent(sensorsToSend::add);

        SensorDataParser dataParser = parseType.equals("json") ? new JsonSensorParser() : new XmlSensorParser();

        return new ResponseEntity<>(dataParser.parseSensorToFormat(sensorsToSend), responseHeaders, HttpStatus.OK);
    }
}

