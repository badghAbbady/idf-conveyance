package esiag.back.repositories.sample;


import esiag.back.models.sample.Route;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepositry extends CrudRepository<Route,Long> {



}
