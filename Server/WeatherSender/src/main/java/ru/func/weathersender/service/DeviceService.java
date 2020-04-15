package ru.func.weathersender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.func.weathersender.entity.Device;
import ru.func.weathersender.repository.DeviceRepository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author func 04.01.2020
 */
@Service
public class DeviceService {

    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Optional<Device> create(String author, String deviceName, String data) {
        if (deviceRepository.findByDeviceName(deviceName).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(deviceRepository.save(Device.builder()
                .author(author)
                .deviceName(deviceName)
                .status(data)
                .build()
        ));
    }
    
    public void update(String deviceName, String data) {
        deviceRepository.findByDeviceName(deviceName).ifPresent(device -> device.setStatus(data));
    }
    
    public String getData(String deviceName) {
        AtomicReference<String> data = new AtomicReference<>("{}");
        deviceRepository.findByDeviceName(deviceName)
                .map(Device::getStatus)
                .ifPresent(data::set);
        return data.get();
    }
}
