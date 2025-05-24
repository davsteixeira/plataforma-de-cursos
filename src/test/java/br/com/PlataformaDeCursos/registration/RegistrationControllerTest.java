package br.com.PlataformaDeCursos.registration;

import br.com.PlataformaDeCursos.course.Course;
import br.com.PlataformaDeCursos.course.CourseRepository;
import br.com.PlataformaDeCursos.course.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private RegistrationRepository registrationRepository;

    @Test
    void createRegistration__withValidData_shouldReturn201() throws Exception {
        NewRegistrationDTO dto = new NewRegistrationDTO();
        dto.setCourseCode("JAVA101");
        dto.setStudentEmail("student@example.com");

        Course course = new Course();
        course.setCode("JAVA101");
        course.setStatus(Status.ACTIVE);

        Mockito.when(courseRepository.findById("JAVA101")).thenReturn(Optional.of(course));
        Mockito.when(registrationRepository.existsByCourseCodeAndStudentEmail("JAVA101", "student@example.com")).thenReturn(false);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createRegistration__withNonexistentCourse_shouldReturn400() throws Exception {
        NewRegistrationDTO dto = new NewRegistrationDTO();
        dto.setCourseCode("INVALID");
        dto.setStudentEmail("student@example.com");

        Mockito.when(courseRepository.findById("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void createRegistration__withInactiveCourse_shouldReturn400() throws Exception {
        Course course = new Course();
        course.setCode("JAVA101");
        course.setStatus(Status.INACTIVE);

        NewRegistrationDTO dto = new NewRegistrationDTO();
        dto.setCourseCode("JAVA101");
        dto.setStudentEmail("student@example.com");

        Mockito.when(courseRepository.findById("JAVA101")).thenReturn(Optional.of(course));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unable to register for an inactive course"));
    }

    @Test
    void createRegistration__withAlreadyRegisteredStudent_shouldReturn400() throws Exception {
        Course course = new Course();
        course.setCode("JAVA101");
        course.setStatus(Status.ACTIVE);

        NewRegistrationDTO dto = new NewRegistrationDTO();
        dto.setCourseCode("JAVA101");
        dto.setStudentEmail("student@example.com");

        Mockito.when(courseRepository.findById("JAVA101")).thenReturn(Optional.of(course));
        Mockito.when(registrationRepository.existsByCourseCodeAndStudentEmail("JAVA101", "student@example.com")).thenReturn(true);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The student is already registered for this course"));
    }

    @Test
    void report__withDataAvailable_shouldReturn200() throws Exception {
        ArrayList<RegistrationReportItem> reportItems = new ArrayList<>();
        reportItems.add(new RegistrationReportItem("Java Basics", "JAVA101", "John Doe", "instructor@example.com", 10L));

        Mockito.when(registrationService.generateReport()).thenReturn(reportItems);

        mockMvc.perform(get("/registration/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Java Basics"))
                .andExpect(jsonPath("$[0].courseCode").value("JAVA101"))
                .andExpect(jsonPath("$[0].totalRegistrations").value(10));
    }

    @Test
    void report__withNoDataAvailable_shouldReturn204() throws Exception {
        Mockito.when(registrationService.generateReport()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/registration/report"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("No registration data available"));
    }
}

