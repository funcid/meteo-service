package ru.func.weathersender.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.func.weathersender.entity.User;
import ru.func.weathersender.repository.UserRepository;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * @author func 11.01.2020
 */
@Slf4j
@Service
public class UserService {

    @Value("${spring.config.name}")
    private String projectName;

    private static final Random RANDOM = new Random();
    private static final String EMAIL_MESSAGE = "Здравствуйте, %s! \n" +
            "Что бы завершить регистрацию %s перейдите по ссылке:\n" +
            "https://func-weather.herokuapp.com/activate/%s\n" +
            "Спасибо за использование нашего сервиса!\n" +
            "%d";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    public boolean addUser(User user) {
        Optional<User> userFromDb = userRepository.findByLogin(user.getLogin());
        if (userFromDb.isPresent())
            return false;
        user.setActivated(false);
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getMail())) {
            log.info("{} было отправленно письмо.", user.getLogin());
            String message = String.format(EMAIL_MESSAGE,
                    user.getLogin(),
                    projectName,
                    user.getActivationCode(),
                    RANDOM.nextInt(9000) + 1000
            );
            mailSender.send(user.getMail(), projectName + " подтвердите аккаунт", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        Optional<User> user = userRepository.findByActivationCode(code);
        if (!user.isPresent())
            return false;
        user.ifPresent(dbUser -> {
            dbUser.setActivationCode(null);
            dbUser.setActivated(true);
            userRepository.save(dbUser);
        });
        return true;
    }

    public boolean successfulLogin(String login, String password) {
        return userRepository.findByLogin(login)
                .filter(User::isActivated)
                .filter(user -> user.getPassword().equals(password))
                .isPresent();
    }
}
