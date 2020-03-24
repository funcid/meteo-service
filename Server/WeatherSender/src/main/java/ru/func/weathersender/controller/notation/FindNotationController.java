package ru.func.weathersender.controller.notation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.func.weathersender.entity.Notation;
import ru.func.weathersender.repository.NotationRepository;
import ru.func.weathersender.util.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author func 04.01.2020
 */
@Slf4j
@Controller
public class FindNotationController {

    @Autowired
    protected NotationRepository notationRepository;

    @RequestMapping(path = "/findSensorById", method = POST)
    public ModelAndView findNotationById(@RequestParam Integer id) {
        ModelAndView modelAndView = new ModelAndView("findSensorById");
        modelAndView.addObject("id", id);

        Optional<Notation> notationObject = notationRepository.findById(id)
                .filter(Notation::getIsPublic);
        notationObject.ifPresent(sensor -> modelAndView.addObject("sensor", sensor));

        log.info(
                "Попытка найти записи c id {}, статус: {}.",
                id,
                notationObject.isPresent() ? "запись найдена" : "запись не найдена"
        );
        return modelAndView;
    }

    @RequestMapping(path = "/findSensorsByLocation", method = POST)
    public ModelAndView findNotationsByLocation(@RequestParam String location) {
        ModelAndView modelAndView = new ModelAndView("findSensorsByLocation");

        modelAndView.addObject("location", location);

        List<Notation> reversedList = new ArrayList<>();

        if (Location.containsName(location)) {
            Location locationObject = Location.valueOf(location.toUpperCase());
            reversedList = notationRepository.findByLocationAndIsPublic(locationObject.getCords(), true).stream()
                    .filter(Notation::getIsPublic)
                    .collect(Collectors.toList());
            Collections.reverse(reversedList);
        }

        modelAndView.addObject("sensors", reversedList);

        log.info(
                "Попытка найти записи из {}, статус: {}.",
                location,
                reversedList.size() == 0 ? "записи не найдены" : "записи найдены"
        );
        return modelAndView;
    }

    @RequestMapping(path = "/findSensorsByTimestamp", method = POST)
    public ModelAndView findNotationsByTimestamp(@RequestParam String timestamp) {
        ModelAndView modelAndView = new ModelAndView("findSensorsByTimestamp");
        modelAndView.addObject("timestamp", timestamp);

        List<Notation> list = notationRepository.findByTimestampAndIsPublic(timestamp, true).stream()
                .filter(Notation::getIsPublic)
                .collect(Collectors.toList());

        modelAndView.addObject("sensors", list);

        log.info(
                "Попытка найти записи полученные в {}, статус: {}.",
                timestamp,
                list.size() == 0 ? "записи не найдены" : "записи найдены"
        );
        return modelAndView;
    }
}
