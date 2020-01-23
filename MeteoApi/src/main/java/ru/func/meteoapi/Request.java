package ru.func.meteoapi;

import lombok.AllArgsConstructor;

/**
 * @author func 23.01.2020
 */
@AllArgsConstructor
public enum Request {

    REQUEST_NEW_DATA("mobile"),
    REQUEST_BY_ID("byId?id=%d"),
    REQUEST_BY_LOCATION("byLocation?location=%s"),
    REQUEST_BY_TIMESTAMP("byTimestamp?timestamp=%s"),;

    private String request;

    @Override
    public String toString() {
        return this.request;
    }
}
