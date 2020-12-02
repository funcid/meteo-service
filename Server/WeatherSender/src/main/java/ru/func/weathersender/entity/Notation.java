package ru.func.weathersender.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author func 11.04.2020
 * @project WeatherSender
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notations")
public class Notation {

    @Id
    @GenericGenerator(name = "increment", strategy = "increment")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String timestamp;
    @Column(nullable = false)
    private Float temperature;
    @Column(nullable = false)
    private Float pressure;
    @Column(nullable = false)
    private Float humidity;
    @Column(nullable = false)
    private String addition;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private Boolean isPublic;
}
