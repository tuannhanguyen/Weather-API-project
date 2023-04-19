package com.skyapi.weatherforescast;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

public class IP2LocationTest {

    private String DBPath = "ip2locdb/IP2LOCATION-LITE-DB3.BIN";

    @Test
    public void testInvalidIP() throws IOException {
        IP2Location ip2Locator = new IP2Location();
        ip2Locator.Open(DBPath);

        String ipAddress = "abc";
        IPResult ipResult = ip2Locator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");

        System.out.print(ipResult);
    }

    @Test
    public void testValidIP1() throws IOException {
        IP2Location ip2Locator = new IP2Location();
        ip2Locator.Open(DBPath);

        String ipAddress = "108.30.178.78";
        IPResult ipResult = ip2Locator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("OK");
        assertThat(ipResult.getCity()).isEqualTo("New York City");

        System.out.print(ipResult);
    }

    @Test
    public void testValidIP2() throws IOException {
        IP2Location ip2Locator = new IP2Location();
        ip2Locator.Open(DBPath);

        String ipAddress = "112.213.94.213";
        IPResult ipResult = ip2Locator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("OK");
        assertThat(ipResult.getCity()).isEqualTo("Ho Chi Minh City");

        System.out.print(ipResult);
    }

}
