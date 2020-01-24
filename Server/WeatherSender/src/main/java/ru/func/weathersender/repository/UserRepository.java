package ru.func.weathersender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.func.weathersender.entity.User;

import java.util.Optional;

/**
 * @author func 11.01.2020
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    Optional<User> findByActivationCode(String activationCode);

}
