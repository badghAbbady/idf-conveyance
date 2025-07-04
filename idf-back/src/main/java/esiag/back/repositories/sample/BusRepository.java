package esiag.back.repositories.sample;

import esiag.back.models.sample.Bus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusRepository extends CrudRepository<Bus,Long> {
}
