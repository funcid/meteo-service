package ru.func.weathersender.controller.notation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author func 18.01.2020
 */
@Controller
public class SensorMenuController {

    @RequestMapping("/sensors")
    public String sensors() {
        return "sensors";
    }
}
