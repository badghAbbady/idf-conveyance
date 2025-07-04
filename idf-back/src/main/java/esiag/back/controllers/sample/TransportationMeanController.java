package esiag.back.controllers.sample;


import esiag.back.models.sample.Registry;
import esiag.back.models.sample.TransportationMean;
import esiag.back.services.sample.TransportationMeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("transportationMean")
public class TransportationMeanController {

    @Autowired
    private TransportationMeanService transportationMeanService;

    @GetMapping("busLignes")
    public ResponseEntity<List<String>> getAllBusLignes() {
        return new ResponseEntity<>(transportationMeanService.getAllBusLignes(), HttpStatus.OK);
    }
    @GetMapping("all")
    public ResponseEntity<Iterable<TransportationMean>> findAllTM() {
        return new ResponseEntity<>(transportationMeanService.findAllTM(), HttpStatus.OK);
    }


    @GetMapping("tramLignes")
    public ResponseEntity<List<String>> getAllTramLignes() {
        return new ResponseEntity<>(transportationMeanService.getAllTramLignes(), HttpStatus.OK);
    }
    @GetMapping("AllLignes")
    public ResponseEntity<List<String>> getAllLignes() {
        return new ResponseEntity<>(transportationMeanService.getAllLignes(), HttpStatus.OK);
    }

    @GetMapping("stationsNames/{ligneCode}")
    public ResponseEntity<List<String>> getStationsByLigneCode(@PathVariable String ligneCode) {
        return new ResponseEntity<>(transportationMeanService.getStationsByLigneCode(ligneCode), HttpStatus.OK);
    }



}
