package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.func.weathersender.service.UserService;

/**
 * @author func 11.01.2020
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String auth(
            @RequestParam String login,
            @RequestParam String password
    ) {
        if (userService.successfulLogin(login, password)) {
            log.info("{} успешно авторизовался.", login);
            return "sensors";
        }
        log.info("bad");
        return "login";
    }
}
