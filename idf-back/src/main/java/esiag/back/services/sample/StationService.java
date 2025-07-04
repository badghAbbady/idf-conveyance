package esiag.back.services.sample;

import esiag.back.models.sample.*;
import esiag.back.repositories.sample.StationRepository;
import esiag.back.repositories.sample.TransportationMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StationService {


    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TransportationMeanRepository transportationMeanRepository;


    public Station findByIdStation(Long idStation) {
        Optional<Station> optionalStation = stationRepository.findById(idStation);
        return optionalStation.orElse(null);
    }

    public Iterable<Station> findAllStation() {
        return stationRepository.findAll();
    }


    public boolean deleteStation(Long idStation) {
        Optional<Station> optionalStation = stationRepository.findById(idStation);
        if (optionalStation.isPresent()) {
            optionalStation.ifPresent(sample -> stationRepository.delete(sample));
            return true;
        }
        return false;
    }

    public boolean addStation(Station station) {
        Station stationAdded = stationRepository.save(station);
        if (stationAdded == null) {
            return false;
        }
        return true;
    }

    public Station findByName(String stationName) {
        return stationRepository.findStationByStationName(stationName);

    }

    /*Focntion qui donne la ligne correspondante à deux stations*/
    public Route getStationsRoute(Station departureStation, Station arrivalStation) {
        /*initialization du variable qu'on va retourner*/
        Route route = null;
        List<Route> routes=new ArrayList<>();
        /*on boucle sur toutes les lignes qui passent par la station de départ*/
        for (Route rt : departureStation.getRoutesStation()) {
            /*on boucle ensuite sur les stations pour trouver la ligne qui correspond à la station d'arrivée*/
            for (Station st : rt.getStations()) {
                if (arrivalStation.equals(st)) {
                    route = rt;
                    routes.add(route);
                }
            }
        }
        return routes.get(0);
    }

    public String getStationsLigneCode(String departureStation, String arrivalStation) {
        Station departStation=findByName(departureStation);
        Station arrivStation=findByName(arrivalStation);
        /*initialization des variables*/
        Route route = null;
        List<Route> routes=new ArrayList<>();
        String ligneCode;
        /*on boucle sur toutes les lignes qui passent par la station de départ*/
        for (Route rt : departStation.getRoutesStation()) {
            /*on boucle ensuite sur les stations pour trouver la ligne qui correspond à la station d'arrivée*/
            for (Station st : rt.getStations()) {
                if (arrivStation == st) {
                    route = rt;
                    routes.add(route);
                }
            }
        }
        /*Recuperer la liste des transports qui nous donnent le code de la ligne*/
        List<TransportationMean> tm=routes.get(0).getTransportationMeans();
        ligneCode=tm.get(0).getLigneCode();
        return ligneCode;
    }




    /*Fonction pour récupérer les stations entre deux stations dans le sens direct*/
    public List<Station> getBetweenStationsForward(String departureStation, String arrivalStation){
        /*on récupere la ligne de ses stations*/
        Route route=getStationsRoute(findByName(departureStation), findByName(arrivalStation));
        /*on récupere ensuite tout les stations de cette ligne*/
        List<Station> allStations=route.getStations();
        /*initialization du variable*/
        List<Station> stationsBetween = new ArrayList<>();
        /*un boolean qui va nous indiquer qu'on a trouvé cette station de départ*/
        boolean foundDeparture = false;
        /*on boucle alors sur ces sttaions*/
        for (Station st:allStations){
            /*si une station égal au station de départ on donne au boolean true*/
            if (st.getStationName().equals(departureStation)) {
                foundDeparture = true;
            }
            /*une fois on trouve la station d'arrivé on sort de la boucle et on ajoute les stations*/
            else if (st.getStationName().equals(arrivalStation)) {
                break;
            } else if (foundDeparture) {
                stationsBetween.add(st);
            }
        }
        return stationsBetween;
    }



    /*fonction officielle qui donne les stations entre deux stations dans les deux sens*/
    public List<Station> getBetweenStations(String departureStationName, String arrivalStationName) {
        /*on récupère les stations si on est au sens direct*/
        List<Station> forwardRoute = getBetweenStationsForward(departureStationName, arrivalStationName);
        /*si on est dans l'autre sens de la ligne on renverse les stations tout simplement*/
        if (forwardRoute.isEmpty()) {
            /*Si la route directe est vide, essayez dans l'autre sens*/
            List<Station> reversedRoute=getBetweenStationsForward(arrivalStationName, departureStationName);
            Collections.reverse(reversedRoute);
            return reversedRoute;
        } else {
            return forwardRoute;
        }
    }
    /*Fonction qui retourne le temps entre deux stations*/
    public List<Integer> getTimeBetweenStations(String departureStation,String arrivalStation) {
        /*on récupère l'objet station en utilisant les noms*/
        Station departStation = findByName(departureStation);
        Station arrivStation = findByName(arrivalStation);
        /*on récupère la ligne de ces stations*/
        Route route = getStationsRoute(departStation, arrivStation);
        /*on récupère toutes les stations de cette ligne*/
        List<Station> routeStations = route.getStations();
        /*on récupère le schedule de cette ligne*/
        List<Schedule> schedules = arrivStation.getSchedules();
        /*on récupère les durées entre les stations de cette ligne*/
        List<Integer> durations = route.getTransportationMeans().get(0).getSchedule().getDurations();
        /*on récupère les indices de ces stations demandées puisque les stations de la ligne sont triées'*/
        int departureIndex = routeStations.indexOf(departStation);
        int arrivalIndex = routeStations.indexOf(arrivStation);
        /*on initialise la liste des durées qu'on va récuperer*/
        List<Integer> durationsSlice = new ArrayList<>();
        /*on vérifie si le sens des stations est direct*/
        if (arrivalIndex > departureIndex) {
            /*si oui on récupère les durations entre ces stations puisque les durations sont aussi triés*/
            durationsSlice = durations.subList(departureIndex, arrivalIndex);
        } else {
            /*sinon on renverse tout simplement la liste*/
            durationsSlice = durations.subList(arrivalIndex, departureIndex);
            Collections.reverse(durations.subList(arrivalIndex, departureIndex));

        }
        return durationsSlice;
    }
        public List<String> getTerminusOfRoute (String ligneCode){
            List<TransportationMean> tm = (List<TransportationMean>) transportationMeanRepository.getTransportationMeanByLigneCodeIs(ligneCode);
            Route route = tm.get(0).getRoute();
            return Arrays.asList(route.getArrivalStation(), route.getDepartureStation());
        }


    public String getTerminusOf2Stations(String departureStation,String arrivalStation){
        Route route =getStationsRoute(findByName(departureStation),findByName(arrivalStation));
        List<Station> stations=route.getStations();
        String terminus;
        if (stations.indexOf(findByName(departureStation))<stations.indexOf(findByName(arrivalStation))){
            terminus=route.getArrivalStation();
        }else{
            terminus=route.getDepartureStation();
        }

        return terminus;
    }
    }








