package esiag.back.repositories.sample;

import esiag.back.models.sample.TransportationMean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransportationMeanRepository extends CrudRepository<TransportationMean,Long> {
    public List<TransportationMean> getTransportationMeanByLigneCodeIs(String ligneCode);
}
