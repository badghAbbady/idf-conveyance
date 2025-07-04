package esiag.back.controllers.sample;


import esiag.back.models.sample.HistoryOfRegistry;
import esiag.back.models.sample.Registry;
import esiag.back.repositories.sample.HistoryOfRegistryRepository;
import esiag.back.services.sample.HistoryOfRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("history")
public class HistoryOfRegistryController {
    @Autowired
    private HistoryOfRegistryService historyOfRegistryService;

    @GetMapping("/{id}")
    public ResponseEntity<HistoryOfRegistry> findById(@PathVariable Long id){
        return new ResponseEntity<>(historyOfRegistryService.findByIdHistory(id), HttpStatus.OK);
    }

    @PostMapping("add")
    public boolean addRegistryToHistory(
            @RequestBody HistoryOfRegistry history) {
        historyOfRegistryService.addRegistryToHistory(history);
        return true;
    }

    @GetMapping("all")
    public ResponseEntity<Iterable<HistoryOfRegistry>> findAllHistory() {
        return new ResponseEntity<>(historyOfRegistryService.findAllHistory(), HttpStatus.OK);
    }
}
