package ru.func.weathersender.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author func 23.01.2020
 */
@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "variables")
public class Device {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String deviceName;
    @Column(nullable = false)
    private String status;
}