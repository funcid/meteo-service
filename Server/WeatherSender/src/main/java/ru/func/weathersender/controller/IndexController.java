package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.func.weathersender.entity.Notation;
import ru.func.weathersender.entity.User;
import ru.func.weathersender.repository.NotationRepository;
import ru.func.weathersender.repository.UserRepository;
import ru.func.weathersender.util.Location;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * @author func 06.01.2020
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    protected NotationRepository notationRepository;
    @Autowired
    private UserRepository userRepository;

    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd-k-m", new Locale("ru", "RU"));

    @RequestMapping("/")
    public String index(
            @RequestParam(name = "location", required = false, defaultValue = "none") String location,
            @RequestParam(name = "pressure", required = false) Float pressure,
            @RequestParam(name = "humidity", required = false) Float humidity,
            @RequestParam(name = "temperature", required = false) Float temperature,
            @RequestParam(name = "addition", required = false, defaultValue = "no addition") String addition,
            @RequestParam(name = "login", required = false) String login,
            @RequestParam(name = "password", required = false) String password,
            HttpServletRequest request) {
        if (!location.equals("none") && !Strings.isEmpty(login)) {
            Optional<User> user = userRepository.findByLogin(login);
            if (user.isPresent() && user.get().getPassword().equals(password)) {
                Notation notation = notationRepository.save(Notation.builder()
                        .location(Location.valueOf(location).getCords())
                        .pressure(pressure)
                        .humidity(humidity)
                        .temperature(temperature)
                        .timestamp(dateFormat.format(new Date()))
                        .addition(addition)
                        .author(login)
                        .isPublic(false)
                        .build()
                );
                log.info("Создана новая запись с ID {}. IP отправителя {}.", notation.getId(), request.getRemoteAddr());
            }
        }
        return "index";
    }
}
