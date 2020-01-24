package ru.func.weathersender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.func.weathersender.entity.Notation;
import ru.func.weathersender.repository.NotationRepository;
import ru.func.weathersender.util.Location;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author func 24.01.2020
 */
@Service
public class DataService {

    @Autowired
    protected NotationRepository notationRepository;

    public List<Notation> getNotificationList(boolean isPublic) {
        return Stream.of(Location.values())
                .map(location -> notationRepository.findNewestNotationByLocation(location.getCords(), isPublic))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
