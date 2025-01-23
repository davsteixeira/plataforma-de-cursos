package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    void CreateCourse__withValidInstructor_shouldReturn201() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java Básico");
        newCourseDTO.setCode("java-basic");
        newCourseDTO.setDescription("Curso básico de Java");
        newCourseDTO.setInstructorEmail("instructor@email.com");
        User instructor = new User("Instrutor", "instructor@email.com", Role.INSTRUCTOR, "senha1");

        Mockito.when(userRepository.findByEmail("instructor@email.com")).thenReturn(Optional.of(instructor));
        Mockito.when(courseRepository.existsByCode("java-basic")).thenReturn(false);
        Course course = new Course("java-basic", "Java Básico", "Curso básico de Java", "instructor@email.com", Status.ACTIVE);
        Mockito.when(courseService.createCourse(any(NewCourseDTO.class))).thenReturn(course);

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("java-basic"))
                .andExpect(jsonPath("$.name").value("Java Básico"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void CreateCourse__withNonInstructorUser_shouldReturn400() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java Básico");
        newCourseDTO.setCode("java-basic");
        newCourseDTO.setDescription("Curso básico de Java");
        newCourseDTO.setInstructorEmail("student@email.com");
        User student = new User("Aluno", "student@email.com", Role.STUDENT, "senha1");

        Mockito.when(userRepository.findByEmail("student@email.com")).thenReturn(Optional.of(student));

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("role"))
                .andExpect(jsonPath("$.message").value("The user must be an INSTRUCTOR to create a course"));
    }

    @Test
    void DeactivateCourse__withValidCode_shouldReturn200() throws Exception {

        Course course = new Course("java-basic", "Java Básico", "Curso básico de Java", "instructor@email.com", Status.ACTIVE);
        Mockito.when(courseRepository.findById("java-basic"))
                .thenReturn(Optional.of(course));
        
        course.setStatus(Status.INACTIVE);
        course.setInactivationAt(java.time.LocalDateTime.now());
        Mockito.when(courseService.deactivateCourse(any(Course.class)))
                .thenReturn(course);
        
        mockMvc.perform(post("/course/java-basic/inactive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.code").value("java-basic"));
    }

    @Test
    void DeactivateCourse__withInvalidCode_shouldReturn404() throws Exception {
        Mockito.when(courseRepository.findById("invalid-code"))
                .thenReturn(Optional.empty());
        
        mockMvc.perform(post("/course/invalid-code/inactive"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Course not found with this code"));
    }
}
