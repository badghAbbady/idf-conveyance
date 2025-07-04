package esiag.back.services.sample;

import esiag.back.models.sample.Subscriber;
import esiag.back.repositories.sample.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {
    @Autowired
    SubscriberRepository subscriberRepository;
    public Subscriber findByUsername(String username) {
        return subscriberRepository.findByUsername(username);
    }
    public List<Subscriber> findAll(){return (List<Subscriber>) subscriberRepository.findAll();}
}

