package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity<Object> createCourse(@RequestBody @Valid NewCourseDTO newCourse) {
        User user = userRepository.findByEmail(newCourse.getInstructorEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Usuário não encontrado com este email"));
        }
        if (user.getRole() != Role.INSTRUCTOR) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("role", "O usuário precisa ser um INSTRUCTOR para criar um curso"));
        }
        if (courseRepository.existsByCode(newCourse.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "Já existe um curso com este código"));
        }

        Course course = service.createCourse(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @Transactional
    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<Object> deactivateCourse(@PathVariable("code") String courseCode) {
        Course course = courseRepository.findById(courseCode).orElse(null);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorItemDTO("code", "Curso não encontrado com este código"));
        }

        course = service.deactivateCourse(course);
        return ResponseEntity.ok(course);
    }
}


