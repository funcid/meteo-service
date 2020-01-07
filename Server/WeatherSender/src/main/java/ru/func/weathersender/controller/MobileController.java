package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.func.weathersender.entity.Sensor;
import ru.func.weathersender.parser.XmlSensorParser;
import ru.func.weathersender.util.Location;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author func 06.01.2020
 */
@Slf4j
@RestController
@RequestMapping(path = "/mobile")
public class MobileController extends DatableController {
    private static final String APPLICATION_JSON_VALUE_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8";
    private static final String APPLICATION_XML_VALUE_UTF8 = MediaType.APPLICATION_XML_VALUE + ";charset=utf-8";
    private static final String LOGGER_OUTPUT_MESSAGE = "Свежие записи были оправлены в формате {}. IP получателя {}.";

    @RequestMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE_UTF8)
    public List<Sensor> sendMobileNewDataJson(HttpServletRequest request) {
        log.info(LOGGER_OUTPUT_MESSAGE, "JSON", request.getRemoteAddr());
        return getSensorList();
    }

    @RequestMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_XML_VALUE,
            produces = APPLICATION_XML_VALUE_UTF8)
    public String sendMobileNewDataXml(HttpServletRequest request) throws ParserConfigurationException {
        log.info(LOGGER_OUTPUT_MESSAGE, "XML", request.getRemoteAddr());
        return new XmlSensorParser().parseSensorToFormat(getSensorList());
    }

    private List<Sensor> getSensorList() {
        return Stream.of(Location.values())
                .map(location -> sensorRepository.findNewestSensorByLocation(location.getCords()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}

