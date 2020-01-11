package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.func.weathersender.service.UserService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

    @RequestMapping(path = "/login", method = POST)
    public String addUser(
            @RequestParam String login,
            @RequestParam String password
    ) {
        UserDetails user = userService.loadUserByUsername(login);
        if (user.getPassword().equals(password)) {
            log.info("{} успешно авторизовался.", login);
            return "redirect:/sensors";
        }
        return "login";
    }
}
