package esiag.back.repositories.sample;

import esiag.back.models.sample.Registry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RegistryRepository extends JpaRepository<Registry,Long> {
    @Query("SELECT r FROM Registry r WHERE r.transportationMean.ligneCode = :ligneCode")
    List<Registry> findAllByLigneCode(String ligneCode);

    List<Registry> findAllByTransportationMean_TransportationMeanId(Long transportationMeanId);

}
