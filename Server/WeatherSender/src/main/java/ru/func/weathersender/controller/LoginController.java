package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.func.weathersender.entity.User;
import ru.func.weathersender.repository.UserRepository;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author func 11.01.2020
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/login", method = POST)
    public String addUser(
            @RequestParam String login,
            @RequestParam String password
    ) {
        Optional<User> user = userRepository.findByLogin(login);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            log.info("{} успешно авторизовался.", login);
            return "redirect:/sensors";
        }
        return "login";
    }
}
