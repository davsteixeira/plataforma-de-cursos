package br.com.alura.ProjetoAlura.course;

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

    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping("/course/new")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        Course course = service.createCourse(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    //Colocar um curso como inativo
    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<Course> deactivateCourse(@PathVariable("code") String courseCode) {
        Course course = service.deactivateCourse(courseCode);
        return ResponseEntity.ok(course);
    }

}
