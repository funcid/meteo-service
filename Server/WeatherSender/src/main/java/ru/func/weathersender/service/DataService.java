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

    public List<Notation> getNewNotificationList(boolean isPublic) {
        return Stream.of(Location.values())
                .map(location -> notationRepository.findNewestNotationByLocation(location.getCords(), isPublic))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Notation> getNewNotificationListByAuthor(String author) {
        return Stream.of(Location.values())
                .map(location -> notationRepository.findNewestNotationByAuthor(author))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Optional<Notation> getNotationById(int id, boolean isPublic) {
        return notationRepository.findById(id)
                .filter(notation -> notation.getIsPublic() == isPublic);
    }

    public List<Notation> getNotationsByLocation(String location, boolean isPublic) {
        return notationRepository.findByLocationAndIsPublic(location, isPublic).stream()
                .filter(Notation::getIsPublic)
                .collect(Collectors.toList());
    }

    public List<Notation> getNotationsByTimestamp(String timestamp, boolean isPublic) {
        return notationRepository.findByTimestampAndIsPublic(timestamp, isPublic).stream()
                .filter(Notation::getIsPublic)
                .collect(Collectors.toList());
    }
}
