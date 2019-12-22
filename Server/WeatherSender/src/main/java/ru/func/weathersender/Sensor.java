package ru.func.weathersender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author func 21.12.2019
 */
@Getter
@AllArgsConstructor
public enum  Sensor {

    CENTER("55.753126, 37.620839", ""),
    SVIBLOVO("55.856336, 37.654575", ""),
    ;

    private String location;
    @Setter
    private String lastData;
}
