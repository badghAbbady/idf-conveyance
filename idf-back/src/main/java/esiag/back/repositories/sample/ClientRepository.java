package esiag.back.repositories.sample;

import esiag.back.models.sample.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client,Long> {
}
