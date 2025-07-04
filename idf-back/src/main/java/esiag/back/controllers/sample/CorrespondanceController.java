package esiag.back.controllers.sample;


import esiag.back.models.sample.Station;
import esiag.back.services.sample.CorrespondanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/correspondance")
public class CorrespondanceController {

    @Autowired
    private CorrespondanceService correspandanceService;

    @GetMapping("/correspondanceStations/{departStation}/{arrivStation}")
    public ResponseEntity<List<Station>> correpondanceStations(@PathVariable String departStation,@PathVariable String arrivStation){
        return new ResponseEntity<>(correspandanceService.correpondanceStations(departStation,arrivStation), HttpStatus.OK);
    }


    @GetMapping("/allPassageStationsCorrespondance/{departStation}/{arrivStation}")
    public ResponseEntity<Map<String,List<Station>>> allPassageStations(@PathVariable String departStation, @PathVariable String arrivStation){
        return new ResponseEntity<>(correspandanceService.allPassageStations(departStation,arrivStation), HttpStatus.OK);
    }


    @GetMapping("/stationBetweenCorrespondance/{departStation}/{arrivStation}")
    public ResponseEntity<List<Station>> stationBetweenCorrespondance(@PathVariable String departStation, @PathVariable String arrivStation){
        return new ResponseEntity<>(correspandanceService.stationBetweenCorrespondance(departStation,arrivStation), HttpStatus.OK);
    }


    @GetMapping("/departureANDarrivalTime/{departStation}/{arrivStation}")
    public ResponseEntity<List<String>> departureANDarrivalTime(@PathVariable String departStation, @PathVariable String arrivStation){
        return new ResponseEntity<>(correspandanceService.departureANDarrivalTime(departStation,arrivStation), HttpStatus.OK);
    }



}
