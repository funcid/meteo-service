package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.func.weathersender.entity.Notation;
import ru.func.weathersender.repository.NotationRepository;
import ru.func.weathersender.util.Location;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author func 06.01.2020
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    protected NotationRepository notationRepository;

    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd-k-m", new Locale("ru", "RU"));

    @RequestMapping("/")
    public String index(
            @RequestParam(name = "loc", required = false, defaultValue = "none") String location,
            @RequestParam(name = "pressure", required = false) Float pressure,
            @RequestParam(name = "humidity", required = false) Float humidity,
            @RequestParam(name = "temperature", required = false) Float temperature,
            HttpServletRequest request) {
        if (!location.equals("none")) {
            Notation notation = notationRepository.save(Notation.builder()
                    .location(Location.valueOf(location).getCords())
                    .pressure(pressure)
                    .humidity(humidity)
                    .temperature(temperature)
                    .timestamp(dateFormat.format(new Date()))
                    .build()
            );
            log.info("Создана новая запись с ID {}. IP отправителя {}.", notation.getId(), request.getRemoteAddr());
        }
        return "index";
    }
}
