package br.com.PlataformaDeCursos.course;

import br.com.PlataformaDeCursos.user.Role;
import br.com.PlataformaDeCursos.user.User;
import br.com.PlataformaDeCursos.user.UserRepository;
import br.com.PlataformaDeCursos.util.ErrorItemDTO;
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
                    .body(new ErrorItemDTO("email", "User not found with this email"));
        }
        if (user.getRole() != Role.INSTRUCTOR) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("role", "The user must be an INSTRUCTOR to create a course"));
        }
        if (courseRepository.existsByCode(newCourse.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "There is already a course with this code"));
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
                    .body(new ErrorItemDTO("code", "Course not found with this code"));
        }

        course = service.deactivateCourse(course);
        return ResponseEntity.ok(course);
    }
}


