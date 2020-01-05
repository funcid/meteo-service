package ru.func.weathersender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.func.weathersender.entity.Sensor;

import java.util.List;
import java.util.Optional;

/**
 * @author func 04.01.2020
 */
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    List<Sensor> findByLocation(String location);

    List<Sensor> findByTimestamp(String timestamp);

    @Query(value = "SELECT *, MAX(id) FROM sensors WHERE location=?1", nativeQuery = true)
    Optional<Sensor> findNewestSensorByLocation(String location);
}
