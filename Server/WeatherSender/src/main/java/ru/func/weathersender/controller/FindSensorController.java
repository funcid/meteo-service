package ru.func.weathersender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.func.weathersender.util.Location;
import ru.func.weathersender.entity.Sensor;
import ru.func.weathersender.repository.SensorRepository;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author func 04.01.2020
 */
@Controller
public class FindSensorController extends DatableController {

    @RequestMapping(path = "/findSensorById", method = POST)
    public ModelAndView findSensorById(@RequestParam Integer id) {
        ModelAndView modelAndView = new ModelAndView("findSensorById");
        modelAndView.addObject("id", id);
        sensorRepository.findById(id)
                .ifPresent(sensor -> modelAndView.addObject("sensor", sensor));

        return modelAndView;
    }

    @RequestMapping(path = "/findSensorsByLocation", method = POST)
    public ModelAndView findSensorsByLocation(@RequestParam String location) {
        ModelAndView modelAndView = new ModelAndView("findSensorsByLocation");

        modelAndView.addObject("location", location);

        List<Sensor> reversedList =
                sensorRepository.findByLocation(Location.valueOf(location.toUpperCase()).getLocation());
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
