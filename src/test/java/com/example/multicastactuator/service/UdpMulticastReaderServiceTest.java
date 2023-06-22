package com.example.multicastactuator.service.service;

import com.example.multicastactuator.MulticastGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UdpMulticastReaderServiceTest {

    @Mock
    private MulticastGroup multicastGroup;

    @InjectMocks
    private UdpMulticastReaderService udpMulticastReaderService;

    private MulticastSocket mockSocket;
    private DatagramPacket mockAdvertisementPacket;
    private DatagramPacket mockWrongPacket;

    @BeforeEach
    void setUp() throws Exception {
        mockSocket = new MulticastSocket();
        mockAdvertisementPacket = new DatagramPacket("advertisement".getBytes(), "advertisement".length());
        mockWrongPacket = new DatagramPacket("wrong".getBytes(), "wrong".length());

        when(multicastGroup.joinGroup("224.0.0.1", 8000)).thenReturn(mockSocket);
        when(multicastGroup.joinGroup("224.0.0.2", 8000)).thenReturn(mockSocket);
    }

    @Test
    void checkAdvertisement_withValidAdvertisement_returnsTrue() throws Exception {
        when(multicastGroup.receive(mockSocket, new byte[256])).thenReturn(mockAdvertisementPacket);

        boolean result = udpMulticastReaderService.checkAdvertisement("224.0.0.1", 8000);

        assertTrue(result, "Expected to receive valid advertisement, but did not");
    }

    @Test
    void checkAdvertisement_withInvalidAdvertisement_returnsFalse() throws Exception {
        when(multicastGroup.receive(mockSocket, new byte[256])).thenReturn(mockWrongPacket);

        boolean result = udpMulticastReaderService.checkAdvertisement("224.0.0.2", 8000);

        assertFalse(result, "Expected not to receive valid advertisement, but did");
    }
}
