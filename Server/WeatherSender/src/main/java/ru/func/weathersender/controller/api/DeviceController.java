package ru.func.weathersender.controller.api;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.func.weathersender.entity.Device;
import ru.func.weathersender.repository.DeviceRepository;
import ru.func.weathersender.service.DeviceService;
import ru.func.weathersender.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author func 23.01.2020
 */
@RestController
public class DeviceController {
    @Autowired
    protected DeviceRepository deviceRepository;
    @Autowired
    protected DeviceService deviceService;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/device")
    public String index(
            @RequestParam(name = "type", required = false, defaultValue = "create") String type,
            @RequestParam(name = "login") String login,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "deviceName") String deviceName,
            @RequestParam(name = "data", required = false, defaultValue = "{}") String data) {
        AtomicReference<String> response = new AtomicReference<>("");
        if (!Strings.isEmpty(login)) {
            userService.successfulLogin(login, password)
                    .flatMap(user -> deviceRepository.findByDeviceName(deviceName))
                    .ifPresent(device -> {
                        if (type.equalsIgnoreCase("create")) {
                            this.deviceService.create(login, deviceName, data);
                            response.set(data);
                        } else if (type.equalsIgnoreCase("get")) {
                            response.set(deviceService.getData(deviceName));
                        } else if (type.equalsIgnoreCase("update")) {
                            this.deviceService.update(deviceName, data);
                            response.set(data);
                        }
                    });
        }
        return response.get();
    }

    @RequestMapping(
            headers = "Accept=application/json",
            produces = "application/json;charset=utf-8",
            path = "/mobile/devices")
    public List<Device> sendMobileNewPrivateDataJson(
            HttpServletRequest request,
            @RequestParam String login,
            @RequestParam String password) {
        List<Device> newNotationsByAuthor = new ArrayList<>();
        userService.successfulLogin(login, password)
                .ifPresent(user -> newNotationsByAuthor.addAll(deviceRepository.findByAuthor(login)));
        return newNotationsByAuthor;
    }
}
