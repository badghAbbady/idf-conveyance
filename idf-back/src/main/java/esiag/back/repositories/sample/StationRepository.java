package esiag.back.repositories.sample;

import esiag.back.models.sample.Station;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StationRepository extends CrudRepository<Station,Long> {
    Station findStationByStationName(String stationName);
}
