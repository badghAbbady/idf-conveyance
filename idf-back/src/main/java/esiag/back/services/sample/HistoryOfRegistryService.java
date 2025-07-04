package esiag.back.services.sample;

import esiag.back.models.sample.HistoryOfRegistry;
import esiag.back.repositories.sample.HistoryOfRegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryOfRegistryService {
    @Autowired
    private HistoryOfRegistryRepository historyOfRegistryRepository;

    public HistoryOfRegistry findByIdHistory(Long idRegistry) {
        Optional<HistoryOfRegistry> optionalHistoryOfRegistry = historyOfRegistryRepository.findById(idRegistry);
        return (HistoryOfRegistry) optionalHistoryOfRegistry.orElse(null);
    }

    public boolean addRegistryToHistory( HistoryOfRegistry historyOfRegistry) {
        HistoryOfRegistry registryAdded=historyOfRegistryRepository.save(historyOfRegistry);
        if (registryAdded==null){
            return false;
        }
        return true;
    }




    public List<HistoryOfRegistry> findAllHistory(){
        return (List<HistoryOfRegistry>) historyOfRegistryRepository.findAll();
    }

}
