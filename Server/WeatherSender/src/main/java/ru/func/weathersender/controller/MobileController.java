package ru.func.weathersender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.func.weathersender.entity.Notation;
import ru.func.weathersender.parser.XmlNotationParser;
import ru.func.weathersender.repository.NotationRepository;
import ru.func.weathersender.service.DataService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

/**
 * @author func 06.01.2020
 */
@Slf4j
@RestController
public class MobileController {

    @Autowired
    protected DataService dataService;

    private static final String APPLICATION_JSON_VALUE_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8";
    private static final String APPLICATION_XML_VALUE_UTF8 = MediaType.APPLICATION_XML_VALUE + ";charset=utf-8";
    private static final String LOGGER_OUTPUT_MESSAGE = "Свежие записи были оправлены в формате {}. IP получателя {}.";

    @RequestMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE_UTF8,
            path = "/mobile")
    public List<Notation> sendMobileNewDataJson(HttpServletRequest request) {
        log.info(LOGGER_OUTPUT_MESSAGE, "JSON", request.getRemoteAddr());
        return dataService.getNotificationList(true);
    }

    @RequestMapping(
            headers = HttpHeaders.ACCEPT + "=" + MediaType.APPLICATION_XML_VALUE,
            produces = APPLICATION_XML_VALUE_UTF8,
            path = "/mobile")
    public String sendMobileNewDataXml(HttpServletRequest request) throws ParserConfigurationException {
        log.info(LOGGER_OUTPUT_MESSAGE, "XML", request.getRemoteAddr());
        return new XmlNotationParser().parseNotationToFormat(dataService.getNotificationList(true));
    }
}