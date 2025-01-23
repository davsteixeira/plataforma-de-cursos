package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void createRegistration__withValidCourseAndStudent_shouldRegister() {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("java-basic");
        newRegistrationDTO.setStudentEmail("student@email.com");

        Course course = new Course("java-basic", "Java Básico", "Curso básico de Java", "instructor@email.com", Status.ACTIVE);

        when(courseRepository.findById("java-basic")).thenReturn(Optional.of(course));
        when(registrationRepository.existsByCourseCodeAndStudentEmail("java-basic", "student@email.com")).thenReturn(false);

        registrationService.createRegistration(newRegistrationDTO);

        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void createRegistration__withInactiveCourse_shouldThrowException() {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("java-basic");
        newRegistrationDTO.setStudentEmail("student@email.com");

        Course course = new Course("java-basic", "Java Básico", "Curso básico de Java", "instructor@email.com", Status.INACTIVE);

        when(courseRepository.findById("java-basic")).thenReturn(Optional.of(course));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registrationService.createRegistration(newRegistrationDTO);
        });

        assertEquals("Cannot register in an inactive course", exception.getMessage());
    }

    @Test
    void createRegistration__withAlreadyRegisteredStudent_shouldThrowException() {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setCourseCode("java-basic");
        newRegistrationDTO.setStudentEmail("student@email.com");
        Course course = new Course("java-basic", "Java Básico", "Curso básico de Java", "instructor@email.com", Status.ACTIVE);

        when(courseRepository.findById("java-basic")).thenReturn(Optional.of(course));
        when(registrationRepository.existsByCourseCodeAndStudentEmail("java-basic", "student@email.com")).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registrationService.createRegistration(newRegistrationDTO);
        });

        assertEquals("Student is already registered in this course", exception.getMessage());
    }

    @Test
    void generateReport__shouldReturnCorrectReport() {
        Object[] courseData1 = new Object[] {
                "Java Básico",
                "java-basic",
                "Instrutor A",
                "instructorA@email.com",
                10L
        };

        Object[] courseData2 = new Object[] {
                "Java Avançado",
                "java-advanced",
                "Instrutor B",
                "instructorB@email.com",
                5L
        };

        List<Object[]> rawResults = List.of(courseData1, courseData2);

        when(registrationRepository.getCourseRegistrationReportRaw()).thenReturn(rawResults);
        var report = registrationService.generateReport();

        assertEquals(2, report.size());
        assertEquals("Java Básico", report.get(0).getCourseName());
        assertEquals(10L, report.get(0).getTotalRegistrations());
        assertEquals("Java Avançado", report.get(1).getCourseName());
        assertEquals(5L, report.get(1).getTotalRegistrations());
    }
}
