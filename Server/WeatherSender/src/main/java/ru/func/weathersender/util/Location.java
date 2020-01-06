package ru.func.weathersender.util;

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

    public static boolean containsName(String name) {
        for(Location location : Location.values())
            if (location.name().equalsIgnoreCase(name))
                return true;

        return false;
    }
}