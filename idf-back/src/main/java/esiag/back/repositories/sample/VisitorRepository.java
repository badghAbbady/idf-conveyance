package esiag.back.repositories.sample;

import esiag.back.models.sample.Visitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends CrudRepository<Visitor,Long>{
    Optional<Visitor> findByEmail(String email);
}
