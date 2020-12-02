package ru.func.weathersender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.func.weathersender.entity.Device;

import java.util.List;
import java.util.Optional;

/**
 * @author func 04.01.2020
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByDeviceName(String name);
    
    @Query(value = "SELECT * FROM variables WHERE author = ?1", nativeQuery = true)
    List<Device> findByAuthor(String author);
}
