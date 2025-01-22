package br.com.alura.ProjetoAlura.registration;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration/new")
    public ResponseEntity<Void> createRegistration(@Valid @RequestBody NewRegistrationDTO dto) {
        registrationService.createRegistration(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<ArrayList<RegistrationReportItem>> report() {
        ArrayList<RegistrationReportItem> report = new ArrayList<>(registrationService.generateReport());
        return ResponseEntity.ok(report);
    }


}
