package ru.func.weathersender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.func.weathersender.repository.NotationRepository;

/**
 * @author func 06.01.2020
 */
public abstract class DatableController {

    @Autowired
    protected NotationRepository notationRepository;
}
