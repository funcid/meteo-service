package ru.func.weathersender;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author func 05.01.2020
 */
@Getter
@AllArgsConstructor
public enum Location {

    CENTER("55.753126, 37.620839"),
    SVIBLOVO("55.856336, 37.654575"),;

    private String location;
}