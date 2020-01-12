package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.func.weathersender.entity.User;
import ru.func.weathersender.repository.UserRepository;

import java.util.Optional;

/**
 * @author func 11.01.2020
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String auth(
            @RequestParam String login,
            @RequestParam String password
    ) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            log.info("{} успешно авторизовался.", login);
            return "sensors";
        }
        return "login";
    }
}
