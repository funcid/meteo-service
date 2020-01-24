package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.func.weathersender.entity.Notation;
import ru.func.weathersender.service.DataService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author func 23.01.2020
 */
@Slf4j
@RestController
public class ApiController {

    @Autowired
    protected DataService dataService;

    private static final String APPLICATION_JSON_VALUE_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8";
    private static final String LOGGER_OUTPUT_MESSAGE = "Свежие записи были оправлены в формате {}. IP получателя {}.";

    @GetMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE_UTF8,
            path = "/api/byId")
    public Notation sendDataById(HttpServletRequest request, @RequestParam int id) {
        log.info(LOGGER_OUTPUT_MESSAGE, "JSON", request.getRemoteAddr());
        return dataService.getNotationById(id, true)
                .orElse(null);
    }

    @GetMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE_UTF8,
            path = "/api/byLocation")
    public List<Notation> sendDataById(HttpServletRequest request, @RequestParam String location) {
        log.info(LOGGER_OUTPUT_MESSAGE, "JSON", request.getRemoteAddr());
        return dataService.getNotationsByLocation(location, true);
    }

    @RequestMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE_UTF8,
            path = "/api/byTimestamp")
    public List<Notation> sendDataByTimestamp(HttpServletRequest request, @RequestParam String timestamp) {
        log.info(LOGGER_OUTPUT_MESSAGE, "JSON", request.getRemoteAddr());
        return dataService.getNotationsByTimestamp(timestamp, true);
    }
}