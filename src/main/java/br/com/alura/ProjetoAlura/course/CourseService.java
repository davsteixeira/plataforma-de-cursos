package br.com.alura.ProjetoAlura.course;

import org.springframework.stereotype.Service;

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
                dto.getInstructorEmail()
        );

        return repository.save(course);
    }
}