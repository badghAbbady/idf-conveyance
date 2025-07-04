package esiag.back.services.sample;


import esiag.back.models.sample.*;

import esiag.back.repositories.sample.RegistryRepository;
import esiag.back.repositories.sample.SubscriberRepository;
import esiag.back.repositories.sample.TransportationMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import org.springframework.expression.spel.ast.StringLiteral;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistryService {
    @Autowired
    private RegistryRepository registryRepository;
    @Autowired
    private TransportationMeanService transportationMeanService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private TransportationMeanRepository transportationMeanRepository;
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private  StationService stationService;
    @Autowired
    private HistoryOfRegistryService history;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private long registryIdCounter = 1;

    public Registry findByIdRegistry(Long idRegistry) {
        Optional<Registry> optionalRegistry = registryRepository.findById(idRegistry);
        return optionalRegistry.orElse(null);
    }

    public List<Registry> findAllRegistry(){
        return (List<Registry>) registryRepository.findAll();
    }

    public boolean deleteRegistry(Long idRegistry) {
        Optional<Registry> optionalRegistry = registryRepository.findById(idRegistry);
        if (optionalRegistry.isPresent()) {
            optionalRegistry.ifPresent(sample -> registryRepository.delete(sample));
            return true;
        }
        return false;
    }

    public boolean addRegistry(Registry registry){
        Registry registryAdded=registryRepository.save(registry);
        if (registryAdded==null){
            return false;
        }
        return true;
    }

    public List<Registry> findRegistriesByLigneCode(String ligneCode) {
        return registryRepository.findAllByLigneCode(ligneCode);
    }

    public List<Registry> findAllByTransportationMeanId(Long transportationMeanId) {
        return registryRepository.findAllByTransportationMean_TransportationMeanId(transportationMeanId);
    }


    public int visitorCounter(String localtime ,Long tmId){
        Optional<TransportationMean> tm= Optional.ofNullable(transportationMeanService.findTMById(tmId));
        System.out.println(tm);
        String ligneCode=tm.get().getLigneCode();
         String terminus1= tm.get().getRoute().getArrivalStation();
         String terminus2= tm.get().getRoute().getDepartureStation();
        Map<Long,List<Map.Entry<String,String>>> schedule= scheduleService.generateFullSchedules(ligneCode);
        Random rand = new Random();
        List<Map.Entry<String,String>> scheduleTm=schedule.get(tmId);
        //String localtime= formatter.format(LocalTime.now());
        List<Subscriber> subscribers=(List<Subscriber>)subscriberService.findAll();
        for (Map.Entry<String, String> entry :scheduleTm){
            String station= entry.getKey();
            String arrivalTime= entry.getValue();
            String nextStation=scheduleTm.get(scheduleTm.indexOf(entry)+1).getKey();
            List<Station> stations_in_between;
            stations_in_between=stationService.getBetweenStations(station,terminus1);
            stations_in_between.add(stationService.findByName(terminus1));
            if(!stations_in_between.contains(stationService.findByName(nextStation))){
                stations_in_between=stationService.getBetweenStations(station,terminus2);
                stations_in_between.add(stationService.findByName(terminus2));
            }

            if (localtime.equals(arrivalTime)){

                for (int i=0; i< rand.nextInt(50); i ++){
                    Registry registry=new Registry();
                    registry.setIdRegistry(registryIdCounter++);
                    registry.setDateRegistry(formatter1.format(LocalDateTime.now()));
                    registry.setBoardingTime(null);
                    registry.setSubscriber(subscribers.get(rand.nextInt(subscribers.size())));
                    registry.setEntryStation(station);
                    registry.setExitStation(stations_in_between.get(rand.nextInt(stations_in_between.size())).getStationName());
                    registry.setTransportationMean(tm.get());
                    registry.setDestinationTime(null);
                    addRegistry(registry);
                    //adding to the history
                    HistoryOfRegistry historyOfRegistry=new HistoryOfRegistry();
                    historyOfRegistry.setIdHistory(registry.getIdRegistry());
                    historyOfRegistry.setDateRegistry(formatter1.format(LocalDateTime.now()));
                    historyOfRegistry.setBoardingTime(null);
                    historyOfRegistry.setSubscriber(registry.getSubscriber());
                    historyOfRegistry.setEntryStation(station);
                    historyOfRegistry.setExitStation(registry.getExitStation());
                    historyOfRegistry.setTransportationMeanH(tm.get());
                    historyOfRegistry.setDestinationTime(null);
                    history.addRegistryToHistory(historyOfRegistry);

                }
                for(Registry registrys : tm.get().getRegistries()){
                    if(registrys.getExitStation().equals(station) ){
                        deleteRegistry(registrys.getIdRegistry());
                    }
                }
                break;
            }
        }
        System.out.println("hi");
        return findAllByTransportationMeanId(tmId).size();
    }

//    @Scheduled(fixedRate = 10000)
//    public int VisitorCounterScheduled(){
//        List<Long> ids = new ArrayList<>();
//        List<TransportationMean> transportationMeans = (List<TransportationMean>) transportationMeanRepository.findAll();
//        // Iterate over the transportationMeans and collect their IDs
//        transportationMeans.forEach(transportationMean -> ids.add(transportationMean.getTransportationMeanId()));
//        System.out.println(ids);
//        int k=0;
//        for(long id : ids){
//            System.out.println(id);
//            k= visitorCounter(id);
//            System.out.println(k);
//        }
//
//        System.out.println("hthtnh");
//
//        return k;
//    }


}
