package esiag.back.controllers.sample;

import esiag.back.models.sample.HistoryOfRegistry;
import esiag.back.models.sample.Registry;
import esiag.back.models.sample.Subscriber;
import esiag.back.services.sample.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping("/{username}/registries")
    public ResponseEntity<List<HistoryOfRegistry>> getSubscriberRegistries(@PathVariable String username) {
        // search for subscriber by username
        Subscriber subscriber = subscriberService.findByUsername(username);

        if (subscriber != null) {
            // fetch the subscriber's registries
            List<HistoryOfRegistry> registries = subscriber.getHistory();
            return new ResponseEntity<>(registries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
