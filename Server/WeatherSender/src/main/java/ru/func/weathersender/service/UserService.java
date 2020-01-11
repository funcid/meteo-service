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
import java.util.UUID;

/**
 * @author func 11.01.2020
 */
@Slf4j
@Service
public class UserService implements UserDetailsService {
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
        userFromDb.ifPresent(dbUser -> {
            dbUser.setActivated(true);
            dbUser.setActivationCode(UUID.randomUUID().toString());

            userRepository.save(dbUser);
        });


        if (!StringUtils.isEmpty(user.getMail())) {
            log.info("{} было отправленно письмо.", user.getUsername());
            String message = String.format(
                    "Здравствуйте, %s! \n" +
                            "Что бы завершить регистрауию WeatherService перейдите по ссылке: https://func-weather.herokuapp.com/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getMail(), "Activation code", message);
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
