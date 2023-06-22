package com.example.multicastactuator.actuator;

import com.example.multicastactuator.service.UdpMulticastReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "multicast")
public class MulticastEndpoint {

    @Autowired
    private UdpMulticastReaderService udpMulticastReaderService;

    @ReadOperation
    public Map<String, Object> checkAdvertisement(@Selector String address, @Selector int port) {
        Map<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("port", port);

        try {
            boolean isAdvertisementAvailable = udpMulticastReaderService.checkAdvertisement(address, port);
            result.put("available", isAdvertisementAvailable);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("available", false);
        }

        return result;
    }
}
