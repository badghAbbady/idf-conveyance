// VisitorController.java
package esiag.back.controllers.sample;

import esiag.back.models.sample.Visitor;
import esiag.back.services.sample.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("visitor")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @GetMapping("/byEmail")
    public ResponseEntity<Visitor> findByEmail(@RequestParam String email) {
        Optional<Visitor> optionalVisitor = visitorService.findByEmail(email);
        return optionalVisitor.map(visitor -> new ResponseEntity<>(visitor, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value="/save",consumes = "application/json")
    public ResponseEntity<Visitor> saveVisitor(@RequestBody Visitor visitor) {
        Visitor savedVisitor = visitorService.saveVisitor(visitor);
        if (savedVisitor != null) {
            return new ResponseEntity<>(savedVisitor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
