package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {

    private final CourseService service;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CourseController(CourseService service, UserRepository userRepository, CourseRepository courseRepository) {
        this.service = service;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/course/new")
    public ResponseEntity<Object> createCourse(@RequestBody @Valid NewCourseDTO newCourse) {
        List<ErrorItemDTO> errors = new ArrayList<>();

        // Como ainda não há um login para que eu possa pegar informações do token, estou acessando direto no banco de usuários para verificar se o usuário é um instrutor
        User user = userRepository.findByEmail(newCourse.getInstructorEmail())
                .orElse(null);
        if (user == null) {
            errors.add(new ErrorItemDTO("email", "Usuário não encontrado com este email"));
        } else if (user.getRole() != Role.INSTRUCTOR) {
            errors.add(new ErrorItemDTO("role", "O usuário precisa ser um INSTRUCTOR para criar um curso"));
        }

        if (courseRepository.existsByCode(newCourse.getCode())) {
            errors.add(new ErrorItemDTO("code", "Já existe um curso com este código"));
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Course course = service.createCourse(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    // Colocar um curso como inativo
    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<Course> deactivateCourse(@PathVariable("code") String courseCode) {
        Course course = service.deactivateCourse(courseCode);
        return ResponseEntity.ok(course);
    }
}

