package ru.func.weathersender.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author func 05.01.2020
 */
@Getter
@AllArgsConstructor
public enum Location {

    CENTER("55.753126, 37.620839"),
    SVIBLOVO("55.856336, 37.654575"),;

    private String cords;

    public static boolean containsName(String name) {
        return Stream.of(Location.values()).anyMatch(location -> location.name().equalsIgnoreCase(name));
    }
}