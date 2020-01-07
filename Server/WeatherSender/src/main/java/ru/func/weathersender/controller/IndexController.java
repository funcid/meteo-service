package ru.func.weathersender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.func.weathersender.entity.Sensor;
import ru.func.weathersender.util.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author func 06.01.2020
 */
@Controller
public class IndexController extends DatableController {

    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd-k-m", new Locale("ru", "RU"));

    @RequestMapping("/")
    public String index(
            @RequestParam(name = "loc", required = false, defaultValue = "none") String location,
            @RequestParam(name = "pressure", required = false) Float pressure,
            @RequestParam(name = "humidity", required = false) Float humidity,
            @RequestParam(name = "temperature", required = false) Float temperature) {
        if (!location.equals("none")) {
            sensorRepository.save(Sensor.builder()
                    .location(Location.valueOf(location).getCords())
                    .pressure(pressure)
                    .humidity(humidity)
                    .temperature(temperature)
                    .timestamp(dateFormat.format(new Date()))
                    .build()
            );
        }
        return "index";
    }
}
