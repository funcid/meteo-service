package ru.func.weathersender.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserService implements UserDetailsService {
    private Random random = new Random();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).orElse(null);
    }

    public boolean addUser(User user) {
        Optional<User> userFromDb = userRepository.findByLogin(user.getLogin());
        if (userFromDb.isPresent())
            return false;
        user.setActivated(true);
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getMail())) {
            log.info("{} было отправленно письмо.", user.getUsername());
            String message = String.format(
                    "Здравствуйте, %s! \n" +
                            "Что бы завершить регистрацию WeatherService перейдите по ссылке:\n" +
                            "https://func-weather.herokuapp.com/activate/%s\n" +
                            "Спасибо за использование нашего сервиса!\n" +
                            "%d",
                    user.getUsername(),
                    user.getActivationCode(),
                    random.nextInt(9000)+1000
            );

            mailSender.send(user.getMail(), "MeteoService подтвердите аккаунт", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        Optional<User> user = userRepository.findByActivationCode(code);
        if (!user.isPresent())
            return false;
        user.ifPresent(dbUser -> {
            dbUser.setActivationCode(null);
            userRepository.save(dbUser);
        });
        return true;
    }
}
