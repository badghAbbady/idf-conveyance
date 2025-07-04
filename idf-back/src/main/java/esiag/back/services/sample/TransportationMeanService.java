package esiag.back.services.sample;

import esiag.back.models.sample.*;
import esiag.back.repositories.sample.BusRepository;
import esiag.back.repositories.sample.ScheduleRepository;
import esiag.back.repositories.sample.TramRepository;
import esiag.back.repositories.sample.TransportationMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.OverridesAttribute;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class TransportationMeanService {


    @Autowired
    private BusRepository busRepository;
    @Autowired
    private TramRepository tramRepository;
    @Autowired
    private TransportationMeanRepository transportationMeanRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;


    public List<String> getAllLignes(){
        List<String> lignes = new ArrayList<>();
        for(TransportationMean tm :transportationMeanRepository.findAll()){
            if(!lignes.contains(tm.getLigneCode())){
                lignes.add(tm.getLigneCode());
            }
        }
        return lignes;
    }

    public List<String> getAllBusLignes() {
        List<String> busLignesFilter = new ArrayList<String>();
        List<String> busLignes = new ArrayList<String>();
        Set<String> elementsCheked = new HashSet<>();
        for (Bus bus : busRepository.findAll()) {
            busLignesFilter.add(bus.getLigneCode());
        }
        for (String ligne : busLignesFilter) {
            if (!elementsCheked.contains(ligne)) {
                busLignes.add(ligne);
                elementsCheked.add(ligne);
            }
        }
        return busLignes;
    }




        public List<String> getAllTramLignes() {
            List<String> tramLignesFilter = new ArrayList<String>();
            List<String> tramLignes = new ArrayList<String>();
            Set<String> elementsCheked = new HashSet<>();
            for (Tram tram : tramRepository.findAll()) {
                tramLignesFilter.add(tram.getLigneCode());
            }
            for (String ligne :tramLignesFilter ) {
                if (!elementsCheked.contains(ligne)) {
                    tramLignes.add(ligne);
                    elementsCheked.add(ligne);
                }
            }
            return tramLignes;
    }


    public List<String> getStationsByLigneCode(String ligneCode){
       List<TransportationMean> tm= transportationMeanRepository.getTransportationMeanByLigneCodeIs(ligneCode);
       List<Station> stations=((tm.get(0).getRoute()).getStations());
       List<String> stationsName=new ArrayList<>();
       stations.forEach(
               station -> {
                   stationsName.add(station.getStationName());
               }
       );
       return stationsName;
    }


    public List<TransportationMean> getTmByLigneCode(String lignecode){
        return transportationMeanRepository.getTransportationMeanByLigneCodeIs(lignecode);
    }
    public HashMap<String,List<String>> getSchedulesBYLigneCode (String ligneCode) {
        List<TransportationMean> tm=getTmByLigneCode(ligneCode);
        List<Schedule> schedules=new ArrayList<>();
        List<List<Integer>> durations=new ArrayList<>();
        HashMap<String,List<String>> finalSchedule=new HashMap<>();
        tm.forEach(
                transportationMean -> {
                    schedules.add(transportationMean.getSchedule());
                }
        );
        schedules.forEach(
                schedule -> {
                    durations.add(schedule.getDurations());
                }
        );

        for (int i=0; i< durations.size();i++) {
            String busNumber="bus"+(tm.get(i).getTransportationMeanId()).toString();
            String departureTime=tm.get(i).getSchedule().getDepartureHour();
            List<String> sc=new ArrayList<>();
            sc.add(departureTime);
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time= LocalTime.parse(departureTime,formatter);
            for (int j=0 ; j<durations.get(i).size();j++) {
                time=time.plusMinutes(durations.get(i).get(j));
                String resultTime =time.format(formatter);
                sc.add(resultTime);
            }

            finalSchedule.put(busNumber,sc);
            sc=null;


        }
        return finalSchedule;

    }
    public List<Schedule> getSchedulesBYLigneCode1(String ligneCode) {
        List<TransportationMean> tm=getTmByLigneCode(ligneCode);
        List<Schedule> schedules = new ArrayList<>();
        tm.forEach(transportationMean -> schedules.add(transportationMean.getSchedule()));
        return schedules;
    }

    public List<TransportationMean> findAllTM(){
        return (List<TransportationMean>) transportationMeanRepository.findAll();
    }

    public TransportationMean findTMById(Long idTM) {
        Optional<TransportationMean> optionalTM = transportationMeanRepository.findById(idTM);
        return optionalTM.orElse(null);
    }







}
