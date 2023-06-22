package com.example.multicastactuator.service;

import com.example.multicastactuator.MulticastGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

@Service
public class UdpMulticastReaderService {

    @Autowired
    private MulticastGroup multicastGroup;

    @Value("${multicast.timeout}")
    private int timeout;

    public boolean checkAdvertisement(String multicastAddress, int port) {
        boolean isAdvertisementAvailable = false;
        MulticastSocket socket = null;

        try {
            socket = multicastGroup.joinGroup(multicastAddress, port);
            socket.setSoTimeout(timeout); // set the timeout

            DatagramPacket packet = multicastGroup.receive(socket, new byte[256]);
            String msg = new String(packet.getData(), 0, packet.getLength());

            // Assuming the advertisement message should be "advertisement"
            if ("advertisement".equals(msg)) {
                isAdvertisementAvailable = true;
            }

            multicastGroup.leaveGroup(socket, multicastAddress);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
        }

        return isAdvertisementAvailable;
    }
}
