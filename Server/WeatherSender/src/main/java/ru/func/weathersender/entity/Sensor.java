package ru.func.weathersender.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author func 21.12.2019
 */
@Entity
@Table(name = "sensors")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sensor {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(nullable = false)
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
}
