package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;

    public RegistrationService(RegistrationRepository registrationRepository, CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.courseRepository = courseRepository;
    }

    public void createRegistration(NewRegistrationDTO dto) {

        Course course = courseRepository.findById(dto.getCourseCode())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("Cannot register in an inactive course");
        }

        if (registrationRepository.existsByCourseCodeAndStudentEmail(dto.getCourseCode(), dto.getStudentEmail())) {
            throw new IllegalArgumentException("Student is already registered in this course");
        }

        Registration registration = new Registration(dto.getCourseCode(), dto.getStudentEmail());
        registrationRepository.save(registration);
    }

    //Como a query está em sql nativo eu cheguei na lógica de trazer cada curso como Object e transformar em um RegistrationReportItem, pois não estava conseguindo fazer a conversão direta
    public ArrayList<RegistrationReportItem> generateReport() {
        List<Object[]> rawResults = registrationRepository.getCourseRegistrationReportRaw();

        ArrayList<RegistrationReportItem> report = new ArrayList<>();
        for (Object[] result : rawResults) {
            report.add(new RegistrationReportItem(
                    (String) result[0], // courseName
                    (String) result[1], // courseCode
                    (String) result[2], // instructorName
                    (String) result[3], // instructorEmail
                    ((Number) result[4]).longValue() // totalRegistrations
            ));
        }
        return report;
    }

}
