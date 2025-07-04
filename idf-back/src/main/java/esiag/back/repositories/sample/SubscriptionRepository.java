package esiag.back.repositories.sample;

import esiag.back.models.sample.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription,Long> {
}
