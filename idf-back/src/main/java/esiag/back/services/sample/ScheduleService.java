package esiag.back.services.sample;


import esiag.back.models.sample.Route;
import esiag.back.models.sample.Schedule;
import esiag.back.models.sample.Station;
import esiag.back.repositories.sample.BusRepository;
import esiag.back.repositories.sample.ScheduleRepository;
import esiag.back.repositories.sample.TransportationMeanRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ScheduleService {
    @Autowired
    private BusRepository busRepository;


    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TransportationMeanService transportationMeanService;

    @Autowired
    private StationService stationService;

    @Autowired
    private TransportationMeanRepository transportationMeanRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }


    @Getter
    private Map<Long, List<Map.Entry<String, String>>> perturbatedSchedule = null;


    public Map<Long, List<Map.Entry<String, String>>> generateFullScheduleByStation(String ligneCode, String terminusStation) {
        List<Schedule> schedules = transportationMeanService.getSchedulesBYLigneCode1(ligneCode);
        Map<Long, List<Map.Entry<String, String>>> schedulesMap = new HashMap<>();
        Station terminus = stationService.findByName(terminusStation);

        for (Schedule schedule : schedules) {
            List<Map.Entry<String, String>> schedule_rt = new ArrayList<>();
            List<Integer> durations = schedule.getDurations();
            List<Station> stations = schedule.getStations();
            LocalTime departureHour = LocalTime.parse(schedule.getDepartureHour(), formatter);
            LocalTime arrivalHour = LocalTime.parse(schedule.getArrivalHour(), formatter);
            LocalTime time = departureHour;

            if (stations.indexOf(terminus) == stations.size() - 1) {
                while (time.isBefore(arrivalHour)) {
                    // Forward journey
                    int i = 0;
                    while (i < stations.size()) {
                        schedule_rt.add(new AbstractMap.SimpleEntry<>(stations.get(i).getStationName(), time.format(formatter)));
                        if (i < durations.size()) {
                            time = time.plusMinutes(durations.get(i));
                        }
                        i++;
                        if (time.isAfter(arrivalHour)) {
                            break;
                        }
                    }
                    List<Integer> returnDurations = new ArrayList<>(durations);
                    Collections.reverse(returnDurations);
                    int j = stations.size() - 2;
                    time = time.plusMinutes(returnDurations.get(0));
                    while (j >= 0) {
                        j--;
                        if (j >= 0) {
                            time = time.plusMinutes(returnDurations.get(j));
                            if (time.isAfter(arrivalHour)) {
                                break;
                            }
                        }
                    }
                }
            } else {
                while (time.isBefore(arrivalHour)) {
                    // Forward journey
                    int i = 0;
                    while (i < stations.size()) {
                        if (i < durations.size()) {
                            time = time.plusMinutes(durations.get(i));
                        }
                        i++;
                        if (time.isAfter(arrivalHour)) {
                            break;
                        }
                    }
                    List<Integer> returnDurations = new ArrayList<>(durations);
                    Collections.reverse(returnDurations);
                    int j = stations.size() - 1;
                    time = time.plusMinutes(returnDurations.get(0));
                    while (j >= 0) {
                        schedule_rt.add(new AbstractMap.SimpleEntry<>(stations.get(j).getStationName(), time.format(formatter)));
                        j--;
                        if (j >= 0) {
                            time = time.plusMinutes(returnDurations.get(j));
                            if (time.isAfter(arrivalHour)) {
                                break;
                            }
                        }
                    }
                }
            }
            schedulesMap.put(schedule.getTransportationMean().getTransportationMeanId(), schedule_rt);
        }
        return schedulesMap;
    }


    public Map<Long, List<Map.Entry<String, String>>> generateFullSchedules(String ligneCode) {
        List<Schedule> schedules = transportationMeanService.getSchedulesBYLigneCode1(ligneCode);
        Map<Long, List<Map.Entry<String, String>>> schedulesMap = new HashMap<>();

        for (Schedule schedule : schedules) {
            List<Map.Entry<String, String>> schedule_rt = new ArrayList<>();
            List<Integer> durations = schedule.getDurations();
            List<Station> stations = schedule.getStations();
            LocalTime departureHour = LocalTime.parse(schedule.getDepartureHour(), formatter);
            LocalTime arrivalHour = LocalTime.parse(schedule.getArrivalHour(), formatter);
            LocalTime time = departureHour;

            while (time.isBefore(arrivalHour)) {
                //Forward journey
                int i = 0;
                while (i < stations.size()) {
                    schedule_rt.add(new AbstractMap.SimpleEntry<>(stations.get(i).getStationName(), time.format(formatter)));
                    if (i < durations.size()) {
                        time = time.plusMinutes(durations.get(i));
                    }
                    i++;
                    if (time.isAfter(arrivalHour)) {
                        break;
                    }
                }
                //Return journey
                List<Integer> returnDurations = new ArrayList<>(durations);
                Collections.reverse(returnDurations);
                int j = stations.size() - 2;
                time = time.plusMinutes(returnDurations.get(0)); // Move to the last station
                while (j >= 0) {
                    schedule_rt.add(new AbstractMap.SimpleEntry<>(stations.get(j).getStationName(), time.format(formatter)));
                    j--;
                    if (j >= 0) {
                        time = time.plusMinutes(returnDurations.get(j));
                        if (time.isAfter(arrivalHour)) {
                            break;
                        }
                    }
                }
            }
            schedulesMap.put(schedule.getTransportationMean().getTransportationMeanId(), schedule_rt);
        }
        return schedulesMap;
    }


    public Map<Long, List<Map.Entry<String, String>>> schedule_real_time(String localTime, String lignecode) {
        Map<Long, List<Map.Entry<String, String>>> fullSchedules = generateFullSchedules(lignecode);
        Map<Long, List<Map.Entry<String, String>>> realTimeSchedules = new HashMap<>();
        LocalTime clientTime = LocalTime.parse(localTime, formatter);
        LocalTime oneHourBeforeClientTime = clientTime.minusHours(1);
        for (Map.Entry<Long, List<Map.Entry<String, String>>> entry : fullSchedules.entrySet()) {
            Long transportationMeanId = entry.getKey();
            List<Map.Entry<String, String>> full_schedule = entry.getValue();
            List<Map.Entry<String, String>> hour_schedule = new ArrayList<>();
            for (Map.Entry<String, String> currentEntry : full_schedule) {
                LocalTime currentArrivalTime = LocalTime.parse(currentEntry.getValue(), formatter);
                if ((currentArrivalTime.equals(oneHourBeforeClientTime) || currentArrivalTime.isAfter(oneHourBeforeClientTime)) &&
                        !currentArrivalTime.isAfter(clientTime)) {
                    hour_schedule.add(currentEntry);
                }
            }
            realTimeSchedules.put(transportationMeanId, hour_schedule);
        }
        return realTimeSchedules;
    }


    public Map<Long, List<Map.Entry<String, String>>> perturbation_real_time(String localTime, String lignecode) {
        Map<Long, List<Map.Entry<String, String>>> realtime_schedule = schedule_real_time(localTime, lignecode);
        String randomStation = getRandomStation(realtime_schedule);
        Map<Long, List<Map.Entry<String, String>>> perturbated_schedule = new HashMap<>();
        Random random = new Random();
        int randomTimeDifference = random.nextInt(10) + 1;
        boolean foundRandomStation = false;

        // Iterate over the realtime_schedule
        for (Map.Entry<Long, List<Map.Entry<String, String>>> entry : realtime_schedule.entrySet()) {
            List<Map.Entry<String, String>> fullSchedule = entry.getValue();
            List<Map.Entry<String, String>> perturbedHourSchedule = new ArrayList<>();

            // Iterate over the full schedule
            for (Map.Entry<String, String> schedule : fullSchedule) {
                if (schedule.getKey().equals(randomStation)) {
                    foundRandomStation = true;
                }
                if (!foundRandomStation) {
                    // Add original schedule up to the random station
                    perturbedHourSchedule.add(schedule);
                } else {
                    // Perturb the time for stations after the random station
                    String originalTime = schedule.getValue();
                    String[] timeParts = originalTime.split(":");
                    int originalHours = Integer.parseInt(timeParts[0]);
                    int originalMinutes = Integer.parseInt(timeParts[1]);
                    int newMinutes = (originalMinutes + randomTimeDifference) % 60; // Ensure it stays within 0-59
                    int newHours = originalHours + (originalMinutes + randomTimeDifference) / 60; // Adjust hours if minutes overflow
                    newHours = newHours % 24; // Ensure it stays within 0-23
                    String newTime = String.format("%02d", newHours) + ":" + String.format("%02d", newMinutes) + " + " + String.valueOf(randomTimeDifference) + " min";
                    // Create a new entry with perturbed time
                    Map.Entry<String, String> perturbedScheduleEntry = new HashMap.SimpleEntry<>(schedule.getKey(), newTime);
                    // Add the perturbed schedule to the perturbed hour schedule
                    perturbedHourSchedule.add(perturbedScheduleEntry);
                }
            }
            // Store the perturbed hour schedule for this entry
            perturbated_schedule.put(entry.getKey(), perturbedHourSchedule);
        }
        this.perturbatedSchedule = perturbated_schedule;

        return perturbated_schedule;
    }

    private String getRandomStation(Map<Long, List<Map.Entry<String, String>>> realtimeSchedule) {
        // Choose a random entry from the first value in the map
        List<Map.Entry<String, String>> firstSchedule = realtimeSchedule.entrySet().iterator().next().getValue();
        Random random = new Random();
        int randomIndex = random.nextInt(firstSchedule.size());
        return firstSchedule.get(randomIndex).getKey();
    }

    public List<String> getNextPassage(String localTime, String departureStation, String arrivalStation) {
        // Obtient le terminus des 2 stations
        String terminus = stationService.getTerminusOf2Stations(departureStation, arrivalStation);
        Route route = stationService.getStationsRoute(stationService.findByName(departureStation), stationService.findByName(arrivalStation));
        String ligneCode = route.getLignecode();
        Map<Long, List<Map.Entry<String, String>>> schedule = generateFullScheduleByStation(ligneCode, terminus);
        LocalTime localTime1 = LocalTime.parse(localTime, formatter);
        List<String> nextPassage = new ArrayList<>();
        // Itération sur l'horaire
        for (Map.Entry<Long, List<Map.Entry<String, String>>> entry : schedule.entrySet()) {
            List<Map.Entry<String, String>> full_schedule = entry.getValue();
            for (Map.Entry<String, String> currentEntry : full_schedule) {
                LocalTime currentArrivalTime = LocalTime.parse(currentEntry.getValue(), formatter);
                String currentStation = currentEntry.getKey();

                // Vérifie si l'heure d'arrivée actuelle est égale à l'heure locale et la station actuelle est la station de départ
                if (currentArrivalTime.equals(localTime1) && currentStation.equals(departureStation)) {
                    nextPassage.add("Départ imminent");
                    break;
                } else if (currentArrivalTime.isAfter(localTime1) && departureStation.equals(currentStation)) {
                    // Calcule la durée jusqu'au prochain passage
                    Duration duration = Duration.between(localTime1, currentArrivalTime);
                    long minutes = duration.toMinutes();
                    nextPassage.add(minutes + " min");
                    break;
                }
            }
        }

        // Trie la liste de passage, en mettant "Départ imminent" en premier
        nextPassage.sort((a, b) -> {
            if (a.equals("Départ imminent")) {
                return -1; // "Départ imminent" comes first
            } else if (b.equals("Départ imminent")) {
                return 1; // "Départ imminent" comes first
            } else {
                // Sort the minutes in ascending order
                int minA = Integer.parseInt(a.split(" ")[0]);
                int minB = Integer.parseInt(b.split(" ")[0]);
                return Integer.compare(minA, minB);
            }
        });

        return nextPassage;
    }
}