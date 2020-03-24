package ru.func.weathersender.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.func.weathersender.entity.User;
import ru.func.weathersender.service.UserService;
import ru.func.weathersender.util.EmailValidator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author func 11.01.2020
 */
@Slf4j
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    private EmailValidator emailValidator = new EmailValidator();

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @RequestMapping(path = "/registration", method = POST)
    public ModelAndView addUser(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String mail
    ) {
        ModelAndView modelAndView = new ModelAndView("registration");
        StringBuilder message = new StringBuilder("Ошибка регистрации:\n");
        if (password.isEmpty() || password.length() < 4) {
            message.append("Пароль должен быть длиннее 4 и короче 16 знаков.\n");
        } else if (login.length() < 4) {
            message.append("Логин должен быть длиннее 4 и короче 16 знаков.\n");
        } else if (!emailValidator.validate(mail)) {
            message.append("Почта указа не корректно.\n");
        } else if (!userService.addUser(User.builder()
                .login(login)
                .password(password)
                .mail(mail)
                .build()
        )) {
            message.append("Логин занят другим человеком.\n");
        }
        if (message.length() > 20) {
            modelAndView.addObject("message", message.toString());
            return modelAndView;
        } else {
            log.info("{} зарегистрирован успешно.", login);
            return new ModelAndView("login");
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        model.addAttribute("message", isActivated ? "Пользователь был активирован." : "Код подтверждения не найден.");

        return "login";
    }
}