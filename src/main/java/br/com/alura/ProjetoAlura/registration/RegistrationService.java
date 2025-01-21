package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;

    public RegistrationService(RegistrationRepository registrationRepository, CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.courseRepository = courseRepository;
    }

    public void createRegistration(NewRegistrationDTO dto) {
        // Verifica se o curso existe e está ativo
        Course course = courseRepository.findById(dto.getCourseCode())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("Cannot register in an inactive course");
        }

        // Verifica se já existe uma matrícula para o estudante no curso
        if (registrationRepository.existsByCourseCodeAndStudentEmail(dto.getCourseCode(), dto.getStudentEmail())) {
            throw new IllegalArgumentException("Student is already registered in this course");
        }

        // Cria a nova matrícula
        Registration registration = new Registration(dto.getCourseCode(), dto.getStudentEmail());
        registrationRepository.save(registration);
    }

}
