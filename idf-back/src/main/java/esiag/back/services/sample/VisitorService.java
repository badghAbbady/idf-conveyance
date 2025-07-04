// VisitorService.java
package esiag.back.services.sample;

import esiag.back.models.sample.Visitor;
import esiag.back.repositories.sample.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;
    @PostConstruct
    public void init() {
        System.out.println("VisitorService initialized with repository: " + visitorRepository);
    }

    public Visitor saveVisitor(Visitor visitor) {
        try {
            return visitorRepository.save(visitor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Optional<Visitor> findByEmail(String email) {
        return visitorRepository.findByEmail(email);
    }

}
