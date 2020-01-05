package ru.func.weathersender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.func.weathersender.entity.Sensor;
import ru.func.weathersender.repository.SensorRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author func 04.01.2020
 */
@Controller
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-k-m", new Locale("en", "RU"));

    @RequestMapping("/")
    public String index(
            @RequestParam(name = "loc", required=false, defaultValue="none") String location,
            @RequestParam(name = "pressure", required=false) Float pressure,
            @RequestParam(name = "humidity", required=false) Float humidity,
            @RequestParam(name = "temperature", required=false) Float temperature
    ) {
        if (!location.equals("none")) {
            sensorRepository.save(Sensor.builder()
                    .location(location)
                    .pressure(pressure)
                    .humidity(humidity)
                    .temperature(temperature)
                    .timestamp(dateFormat.format(new Date()))
                    .build()
            );
        }
        return "index";
    }

    @RequestMapping(path = "/findSensorById", method = POST)
    public ModelAndView findSensorById(@RequestParam Integer id) {
        ModelAndView modelAndView = new ModelAndView("findSensorById");
        modelAndView.addObject("id", id);

        sensorRepository.findById(id)
                .ifPresent(sensor -> modelAndView.addObject("sensor", sensor));

        return modelAndView;
    }

    @RequestMapping(path = "/findSensorsByLocation", method = POST)
    public ModelAndView findSensorsByCompany(@RequestParam String location) {
        ModelAndView modelAndView = new ModelAndView("findSensorsByLocation");
        modelAndView.addObject("location", location);

        List<Sensor> reversedList = sensorRepository.findByLocation(location);
        Collections.reverse(reversedList);

        modelAndView.addObject("sensors", reversedList);

        return modelAndView;
    }

    @RequestMapping(path = "/findSensorsByTimestamp", method = POST)
    public ModelAndView findSensorsByTimestamp(@RequestParam String timestamp) {
        ModelAndView modelAndView = new ModelAndView("findSensorsByTimestamp");
        modelAndView.addObject("timestamp", timestamp);
        modelAndView.addObject("sensors", sensorRepository.findByTimestamp(timestamp));

        return modelAndView;
    }
}
