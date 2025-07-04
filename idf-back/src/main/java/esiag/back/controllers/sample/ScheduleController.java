package esiag.back.controllers.sample;

import esiag.back.models.sample.Schedule;
import esiag.back.services.sample.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @GetMapping("fullschedule/{lignecode}")
    public ResponseEntity<Map<Long, List<Map.Entry<String, String>>>> getFullSchedule(@PathVariable String lignecode) {
        Map<Long, List<Map.Entry<String, String>>> schedules = scheduleService.generateFullSchedules(lignecode);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/realtimeschedule/{localTime}/{lignecode}")
    public ResponseEntity<Map<Long, List<Map.Entry<String, String>>>> getScheduleRealTime(
            @PathVariable String localTime,
            @PathVariable String lignecode) {
        Map<Long, List<Map.Entry<String, String>>> realTimeSchedules = scheduleService.schedule_real_time(localTime, lignecode);
        return new ResponseEntity<>(realTimeSchedules, HttpStatus.OK);
    }
    @GetMapping("perturbatedSchedule/{localTime}/{lignecode}")
    public ResponseEntity<Map<Long, List<Map.Entry<String, String>>>> getPerturbated(
            @PathVariable String localTime,
            @PathVariable String lignecode) {
        scheduleService.perturbation_real_time(localTime,lignecode);
        Map<Long, List<Map.Entry<String, String>>> perturbated_schedule = scheduleService.getPerturbatedSchedule();
        return ResponseEntity.ok(perturbated_schedule);
    }






    @GetMapping("fullschedule/{ligneCode}/{terminusStation}")
    public ResponseEntity<Map<Long, List<Map.Entry<String, String>>>> getFullScheduleByStation(@PathVariable String ligneCode,@PathVariable String terminusStation) {
        Map<Long, List<Map.Entry<String, String>>> schedule = scheduleService.generateFullScheduleByStation(ligneCode,terminusStation);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("nextPassage/{localTime}/{departureStation}/{arrivalStation}")
    public ResponseEntity<List<String>> getNextPassage(@PathVariable String localTime,@PathVariable String departureStation,@PathVariable String arrivalStation) {
        List<String> nextPassage = scheduleService.getNextPassage(localTime,departureStation,arrivalStation);
        return new ResponseEntity<>(nextPassage, HttpStatus.OK);
    }




}
