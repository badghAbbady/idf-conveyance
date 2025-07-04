package esiag.back.repositories.sample;

import esiag.back.models.sample.HistoryOfRegistry;
import esiag.back.models.sample.Registry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryOfRegistryRepository extends JpaRepository<HistoryOfRegistry,Long> {

}
