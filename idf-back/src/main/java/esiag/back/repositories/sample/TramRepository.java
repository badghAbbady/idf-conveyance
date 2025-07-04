package esiag.back.repositories.sample;

import esiag.back.models.sample.Tram;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramRepository extends CrudRepository<Tram,Long> {
}
