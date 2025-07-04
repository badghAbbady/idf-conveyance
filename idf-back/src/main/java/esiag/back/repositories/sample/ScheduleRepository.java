package esiag.back.repositories.sample;

import esiag.back.models.sample.Schedule;
import esiag.back.models.sample.TransportationMean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleRepository extends CrudRepository<Schedule,Long> {

    public List<Schedule> getScheduleByTransportationMeanIs(TransportationMean transportationMean);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.stations WHERE s.id = :id")
    Schedule findByIdWithStations(@Param("id") Long id);


}
