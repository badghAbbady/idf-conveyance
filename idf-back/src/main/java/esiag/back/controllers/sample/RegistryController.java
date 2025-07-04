package esiag.back.controllers.sample;


import esiag.back.models.sample.Registry;
import esiag.back.services.sample.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController
@RequestMapping("registry")
public class RegistryController {


    @Autowired
    private RegistryService registryService;

    @GetMapping("/{id}")
    public ResponseEntity<Registry> findById(@PathVariable Long id) {
        return new ResponseEntity<>(registryService.findByIdRegistry(id), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<Iterable<Registry>> findAllRegistries() {
        return new ResponseEntity<>(registryService.findAllRegistry(), HttpStatus.OK);
    }

    @GetMapping("/{localtime}/{id}")
    public ResponseEntity<Integer> counter(@PathVariable String localtime, @PathVariable Long id) {
        return new ResponseEntity<>(registryService.visitorCounter(localtime, id), HttpStatus.OK);


    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteRegistry (@PathVariable Long id){
        boolean isRemoved = registryService.deleteRegistry(id);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
    @PostMapping("add")
    public ResponseEntity<Registry> addRegistry (@RequestBody Registry registry){
        if (!registryService.addRegistry(registry)) {
            ;
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        System.out.println(registry);
        return new ResponseEntity<>(registry, HttpStatus.OK);
    }

    @GetMapping("/bylignecode/{ligneCode}")
    public List<Registry> findRegistriesByLignecode (@PathVariable String ligneCode){
        return registryService.findRegistriesByLigneCode(ligneCode);
    }
    @GetMapping("/bytm/{transportationMeanId}")
    public ResponseEntity<List<Registry>> getRegistriesByTransportationMeanId (@PathVariable Long
                                                                                       transportationMeanId){
        List<Registry> registries = registryService.findAllByTransportationMeanId(transportationMeanId);
        return new ResponseEntity<>(registries, HttpStatus.OK);
    }
}



