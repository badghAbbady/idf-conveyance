package esiag.back.services.sample;


import esiag.back.models.sample.Station;
import esiag.back.repositories.sample.RouteRepositry;
import esiag.back.repositories.sample.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteService {
    @Autowired
    private RouteRepositry routeRepositry;

    @Autowired
    private StationService stationService;

    @Autowired
    private ScheduleService scheduleService;




    public Map<Station,String> getBeforeStationAndDuration(String localTime,String departureStation){
        Map<Station,String> reuslt=new HashMap<>();
        Station departStation= stationService.findByName(departureStation);
//        Map<Long, List<Map.Entry<String, String>>> scheduleReelTime=scheduleService.schedule_real_time(localTime);
        return null;
    }
}
