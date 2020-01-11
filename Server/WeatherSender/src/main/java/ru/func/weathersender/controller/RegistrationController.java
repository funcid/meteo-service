package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.func.weathersender.entity.User;
import ru.func.weathersender.service.UserService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author func 11.01.2020
 */
@Slf4j
@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @RequestMapping(path = "/registration", method = POST)
    public String addUser(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String mail
    ) {
        if (!userService.addUser(User.builder()
                .login(login)
                .password(password)
                .mail(mail)
                .build()
        )) {
            return "registration";
        }
        log.info("{} зарегистрирован успешно.", login);
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        model.addAttribute("message", isActivated ? "User successfully activated" : "Activation code is not found!");

        return "login";
    }
}
