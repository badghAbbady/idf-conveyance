package esiag.back;

import esiag.back.models.sample.Station;
import esiag.back.services.sample.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.ParseException;


public class Test {



    public static void main(String[] args) throws ParseException {
        ConfigurableApplicationContext context = SpringApplication.run(EsiagBackApplication.class, args);

        StationService bikeService = context.getBean(StationService.class);
    }
}

