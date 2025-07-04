package esiag.back.controllers.sample;


import esiag.back.models.sample.Station;
import esiag.back.services.sample.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("station")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/{id}")
    public ResponseEntity<Station> findById(@PathVariable Long id){
        return new ResponseEntity<>(stationService.findByIdStation(id), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<Iterable<Station>> findAllStations() {
        return new ResponseEntity<>(stationService.findAllStation(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteStation(@PathVariable Long id){
        boolean isRemoved = stationService.deleteStation(id);
        if(!isRemoved){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(id, HttpStatus.OK);
    }
    @PostMapping("add")
    public ResponseEntity<Station> addStation(@RequestBody Station station) {
        if (stationService.addStation(station) == false) {
            ;
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        System.out.println(station);
        return new ResponseEntity<>(station, HttpStatus.OK);
    }



    @GetMapping("stationsBetween/{departureStation}/{arrivalStation}")
    public ResponseEntity<List<Station>> getBetweenStations(@PathVariable String departureStation,@PathVariable String arrivalStation){
        return new ResponseEntity<>(stationService.getBetweenStations(departureStation,arrivalStation),HttpStatus.OK);
    }

    @GetMapping("timeBetweenStations/{departureStation}/{arrivalStation}")
    public ResponseEntity<List<Integer>> getTimeBetween(@PathVariable String departureStation , @PathVariable String arrivalStation){
        return new ResponseEntity<>(stationService.getTimeBetweenStations(departureStation,arrivalStation),HttpStatus.OK);
    }

    @GetMapping("stationsLigneCode/{departureStation}/{arrivalStation}")
    public ResponseEntity<String> getStationsLigneCode(@PathVariable String departureStation , @PathVariable String arrivalStation){
        return new ResponseEntity<>(stationService.getStationsLigneCode(departureStation,arrivalStation),HttpStatus.OK);
    }

    @GetMapping("routeTerminus/{ligneCode}")
    public ResponseEntity<List<String>> getTerminusOfRoute(@PathVariable String ligneCode){
        return new ResponseEntity<>(stationService.getTerminusOfRoute(ligneCode),HttpStatus.OK);
    }

    @GetMapping("terminusOfTwoStations/{departureStation}/{arrivalStation}")
    public ResponseEntity<String> getTerminusOfTwoStations(@PathVariable String departureStation,@PathVariable String arrivalStation){
        return new ResponseEntity<>(stationService.getTerminusOf2Stations(departureStation,arrivalStation),HttpStatus.OK);
    }





}
