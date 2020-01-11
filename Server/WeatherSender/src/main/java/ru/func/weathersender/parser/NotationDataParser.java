package ru.func.weathersender.parser;

import ru.func.weathersender.entity.Notation;

import java.util.List;

/**
 * @author func 06.01.2020
 */
public interface NotationDataParser {

    String parseNotationToFormat(List<Notation> sensor);
}
