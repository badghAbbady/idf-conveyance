package esiag.back.repositories.sample;

import esiag.back.models.sample.Subscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber,Long> {
    Subscriber findByUsername(String username);
}
