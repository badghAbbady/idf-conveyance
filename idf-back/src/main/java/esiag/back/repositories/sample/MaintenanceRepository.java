package esiag.back.repositories.sample;

import esiag.back.models.sample.Maintenance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MaintenanceRepository extends CrudRepository<Maintenance,Long> {
}
