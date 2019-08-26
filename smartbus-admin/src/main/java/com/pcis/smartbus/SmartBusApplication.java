package com.pcis.smartbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = {"com.pcis.smartbus.filter"})
public class SmartBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartBusApplication.class, args);
    }

}
