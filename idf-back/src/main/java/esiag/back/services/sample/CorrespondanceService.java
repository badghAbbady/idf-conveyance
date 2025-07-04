package esiag.back.services.sample;

import esiag.back.models.sample.Route;
import esiag.back.models.sample.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CorrespondanceService {


    @Autowired
    private StationService stationsService;

    public List<Station>   correpondanceStations (String departStation,String arrivStation) {
        List<Station> correpondanceStation = new ArrayList<>();
        List<Route> departStationRoutes = (stationsService.findByName(departStation)).getRoutesStation();
        List<Route> arrivStationsRoutes = (stationsService.findByName(arrivStation)).getRoutesStation();
        for (Route rt1 : departStationRoutes) {
            List<Station> thisRouteStations = rt1.getStations();
            for (Route rt2 : arrivStationsRoutes) {
                List<Station> thisRouteStations2 = rt2.getStations();
                //Si on est dans la meme ligne, c'est pas la peine de boucler
                if (!rt1.equals(rt2)) {
                    for (Station st1 : thisRouteStations) {
                        for (Station st2 : thisRouteStations2) {
                            if (st1.equals(st2)) {
                                correpondanceStation.add(st1);
                            }
                        }
                    }
                }
            }
        }
        return correpondanceStation;
    }

    public <K, V> Map<K, V> inverserOrdre(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.reverse(list);

        Map<K, V> mapInverse = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            mapInverse.put(entry.getKey(), entry.getValue());
        }
        return mapInverse;
    }




    public Map<String, List<Station>> allPassageStations(String deparStation, String arrivStation) {
        // Recherche des objets Station pour la gare de départ et d'arrivée
        Station departureStation = stationsService.findByName(deparStation);
        Station arrivalStation = stationsService.findByName(arrivStation);

        // Initialization du résultat using LinkedHashMap to maintain insertion order
        Map<String, List<Station>> result = new LinkedHashMap<>();

        // Recherche des correspondances entre les gares de départ et d'arrivée
        List<Station> correspondance = correpondanceStations(deparStation, arrivStation);

        // Vérification si aucune correspondance n'est nécessaire ou si la première correspondance est l'arrivée ou le départ
        if (correspondance.isEmpty() || correspondance.get(0).equals(arrivalStation) || correspondance.get(0).equals(departureStation)) {
            // Création d'une liste de stations pour le trajet sans correspondance
            List<Station> allPassageStations_withoutCorrespondance = new ArrayList<>();
            allPassageStations_withoutCorrespondance.add(departureStation);
            allPassageStations_withoutCorrespondance.addAll(stationsService.getBetweenStations(deparStation, arrivStation));
            allPassageStations_withoutCorrespondance.add(arrivalStation);

            // Ajout de la liste de stations sans correspondance à la Map avec la ligne de code comme clé
            result.put(stationsService.getStationsLigneCode(deparStation, arrivStation), allPassageStations_withoutCorrespondance);
        } else {
            // Ajout de la première correspondance à la Map
            List<Station> allPassageStations_withCorrespondance1 = new ArrayList<>();
            allPassageStations_withCorrespondance1.add(departureStation);
            allPassageStations_withCorrespondance1.addAll(stationsService.getBetweenStations(deparStation, correspondance.get(0).getStationName()));
            allPassageStations_withCorrespondance1.add(correspondance.get(0));
            result.put(stationsService.getStationsLigneCode(deparStation, correspondance.get(0).getStationName()), allPassageStations_withCorrespondance1);

            // Ajout des correspondances intermédiaires à la Map
            for (int i = 0; i < correspondance.size() - 1; i++) {
                List<Station> allPassageStations_withCorrespondance2 = new ArrayList<>();
                allPassageStations_withCorrespondance2.add(correspondance.get(i));
                allPassageStations_withCorrespondance2.addAll(stationsService.getBetweenStations(deparStation, correspondance.get(i + 1).getStationName()));
                allPassageStations_withCorrespondance2.add(correspondance.get(i + 1));
                result.put(stationsService.getStationsLigneCode(correspondance.get(i).getStationName(), correspondance.get(i + 1).getStationName()), allPassageStations_withCorrespondance2);
            }

            // Ajout de la dernière correspondance à la Map
            List<Station> allPassageStations_withCorrespondance3 = new ArrayList<>();
            allPassageStations_withCorrespondance3.add(correspondance.get(correspondance.size() - 1));
            allPassageStations_withCorrespondance3.addAll(stationsService.getBetweenStations(correspondance.get(correspondance.size() - 1).getStationName(), arrivStation));
            allPassageStations_withCorrespondance3.add(arrivalStation);
            result.put(stationsService.getStationsLigneCode(correspondance.get(correspondance.size() - 1).getStationName(), arrivStation), allPassageStations_withCorrespondance3);
        }
        // Retourne la Map résultat contenant les listes de stations pour chaque ligne de code
        return result;
    }







    public List<Station> stationBetweenCorrespondance(String deparStation, String arrivStation) {
        // Recherche des objets Station pour la gare de départ et d'arrivée
        Station departureStation = stationsService.findByName(deparStation);
        Station arrivalStation = stationsService.findByName(arrivStation);

        // Initialization du résultat
        List<Station> result = new ArrayList<>();
        result.add(departureStation);
        List<Station> correspondance = correpondanceStations(deparStation, arrivStation);

        // Vérification si aucune correspondance n'est nécessaire ou si la première correspondance est l'arrivée ou le départ
        if (correspondance.isEmpty() || correspondance.get(0).equals(arrivalStation) || correspondance.get(0).equals(departureStation)) {
            // Création d'une liste de stations pour le trajet sans correspondance
            result.addAll(stationsService.getBetweenStations(deparStation, arrivStation));
        } else {
            result.addAll(stationsService.getBetweenStations(deparStation, correspondance.get(0).getStationName()));
            result.add(correspondance.get(0));
            // Ajout des correspondances intermédiaires à la liste
            for (int i = 0; i < correspondance.size() - 1; i++) {
                result.add(correspondance.get(i));
                result.addAll(stationsService.getBetweenStations(deparStation, correspondance.get(i + 1).getStationName()));
                result.add(correspondance.get(i + 1));
            }
            result.addAll(stationsService.getBetweenStations(correspondance.get(correspondance.size() - 1).getStationName(), arrivStation));

        }
        result.add(arrivalStation);
        return result;
    }

    public List<String> departureANDarrivalTime(String departureStation,String arrivalStation) {
        List <Integer> durationsList=stationsService.getTimeBetweenStations(departureStation,arrivalStation);
        LocalTime departTime = LocalTime.now();
        LocalTime arrivTime=departTime.plusMinutes(durationsList.stream().mapToInt(Integer::intValue).sum());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        String departureTime = departTime.format(format);
        String arrivalTime = arrivTime.format(format);
        return List.of(departureTime,arrivalTime);
    }

}
