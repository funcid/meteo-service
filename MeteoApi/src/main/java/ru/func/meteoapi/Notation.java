package ru.func.meteoapi;

import lombok.Getter;
import lombok.Setter;

/**
 * @author func 21.12.2019
 */
@Getter
@Setter
public class Notation {
    private Integer id;
    private String location;
    private String timestamp;
    private Float temperature;
    private Float pressure;
    private Float humidity;
    private String addition;
    private String author;
    private Boolean isPublic;
}
