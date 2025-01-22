package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationController(RegistrationService registrationService, CourseRepository courseRepository, RegistrationRepository registrationRepository) {
        this.registrationService = registrationService;
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
    }

    @PostMapping("/registration/new")
    public ResponseEntity<Object> createRegistration(@Valid @RequestBody NewRegistrationDTO dto) {
        Course course = courseRepository.findById(dto.getCourseCode()).orElse(null);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("courseCode", "Course not found"));
        }
        if (course.getStatus() != Status.ACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("courseStatus", "Unable to register for an inactive course"));
        }
        if (registrationRepository.existsByCourseCodeAndStudentEmail(dto.getCourseCode(), dto.getStudentEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("registration", "The student is already registered for this course"));
        }

        registrationService.createRegistration(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<Object> report() {
        ArrayList<RegistrationReportItem> report = registrationService.generateReport();
        if (report.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ErrorItemDTO("report", "No registration data available"));
        }
        return ResponseEntity.ok(report);
    }
}
