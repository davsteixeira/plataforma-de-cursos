package br.com.alura.ProjetoAlura.course;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course createCourse(NewCourseDTO dto) {
        Course course = new Course(
                dto.getCode(),
                dto.getName(),
                dto.getDescription(),
                dto.getInstructorEmail(),
                dto.toModel().getStatus()
        );
        return repository.save(course);
    }

    public Course deactivateCourse(Course course) {
        course.setStatus(Status.INACTIVE);
        course.setInactivationAt(LocalDateTime.now());
        return repository.save(course);
    }
}
